package kz.symtech.antifraud.coreservice.services.impl;

import kz.symtech.antifraud.coreservice.dto.ElasticConnectorSearchDto;
import kz.symtech.antifraud.coreservice.enums.ConnectorConflictType;
import kz.symtech.antifraud.coreservice.services.ElasticsearchService;
import kz.symtech.antifraud.models.dto.ElasticSaveRequestDTO;
import kz.symtech.antifraud.models.enums.Fields;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Request;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.reindex.DeleteByQueryRequest;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.ParsedStringTerms;
import org.elasticsearch.search.aggregations.metrics.ParsedSum;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class ElasticsearchServiceImpl implements ElasticsearchService {
    public static final String TERM_COUNT_BY_FIELD = "count_by_field_";
    public static final String TERM_CONNECTOR = "connectorIdAgg";
    private static final String CLASSPATH_SETTINGS = "classpath:settings/elasticsearch-settings.json";
    private static final String CLASSPATH_MAPPINGS = "classpath:mappings/elasticsearch-dynamic-mappings.json";

    private final RestHighLevelClient elasticsearchClient;
    private final ResourceLoader resourceLoader;

    @Value("${index-prefixes.user}")
    private String indexPrefix;

    @Override
    public void createIndex(Long userId) {
        String indexName = indexPrefix + userId;
        log.info("creating index with name {}", indexName);
        CreateIndexRequest request = new CreateIndexRequest(indexName);

        try {
            String jsonMappings = readFileAsString(resourceLoader.getResource(CLASSPATH_MAPPINGS).getURI());

            log.info("adding settings and mappings to index from classpath");
            request.mapping(jsonMappings, XContentType.JSON);
            request.settings(Settings.builder()
                    .loadFromPath(Paths.get(resourceLoader.getResource(CLASSPATH_SETTINGS).getURI()))
                    .build());

            elasticsearchClient.indices().create(request, RequestOptions.DEFAULT);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Map<ConnectorConflictType, Map<String, Long>> countErrorsAndRules() {
        Map<String, Long> errorsCountByConnectorId;
        Map<String, Long> rulesCountByConnectorId;

        SearchRequest searchRequest = new SearchRequest("user1");
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.size(0);

        searchSourceBuilder.aggregation(
                AggregationBuilders
                        .terms(TERM_CONNECTOR)
                        .field(Fields.CONNECTOR_INPUT_ID_FIELD)
                        .subAggregation(AggregationBuilders.sum(Fields.TOTAL_ERRORS_FIELD).field(Fields.ERRORS_COUNT))
                        .subAggregation(AggregationBuilders.sum(Fields.TOTAL_RULES_FIELD).field(Fields.RULES_COUNT)));

        searchRequest.source(searchSourceBuilder);

        SearchResponse searchResponse;
        try {
            searchResponse = elasticsearchClient.search(searchRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        ParsedStringTerms countByFieldAggregation = searchResponse.getAggregations().get(TERM_CONNECTOR);

        errorsCountByConnectorId = fillMap(countByFieldAggregation, Fields.TOTAL_ERRORS_FIELD);
        rulesCountByConnectorId = fillMap(countByFieldAggregation, Fields.TOTAL_RULES_FIELD);

        return Map.of(
                ConnectorConflictType.ERROR, errorsCountByConnectorId,
                ConnectorConflictType.RULE, rulesCountByConnectorId
        );
    }

    @Override
    public List<Object> searchByFields(ElasticConnectorSearchDto elasticConnectorSearchDto) {
        String indexName = indexPrefix + elasticConnectorSearchDto.userId();

        log.info("creating a request to index '{}', to find fields", indexName);
        SearchRequest searchRequest = new SearchRequest(indexName);
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();

        log.info("creating a match query");
        elasticConnectorSearchDto.data()
                .forEach((k, v) -> boolQueryBuilder.must(QueryBuilders.matchQuery(k, v)
                        .fuzziness(Fuzziness.AUTO)));

        sourceBuilder.query(boolQueryBuilder);
        searchRequest.source(sourceBuilder);

        try {
            log.info("handling response of match query");
            return Arrays.stream(elasticsearchClient.search(searchRequest, RequestOptions.DEFAULT)
                            .getHits().getHits())
                    .map(SearchHit::getSourceAsMap)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteExpiredDocuments() {
        List<String> indices = getIndices();
        indices.forEach(index -> {
            log.info("creating request to find documents that 'expiresAt' field value less than or equals to current date");
            QueryBuilder query = QueryBuilders.rangeQuery(Fields.EXPIRES_AT_FIELD).lte("now/d");

            DeleteByQueryRequest deleteRequest = new DeleteByQueryRequest(index);
            deleteRequest.setQuery(query);

            try {
                elasticsearchClient.deleteByQuery(deleteRequest, RequestOptions.DEFAULT);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Override
    public String saveDocument(ElasticSaveRequestDTO elasticSaveRequestDTO) {
        if (!elasticSaveRequestDTO.getSaveResult()) {
            return StringUtils.EMPTY;
        }
        if (elasticSaveRequestDTO.getIncrement()) {
            return sendAddRequestElastic(elasticSaveRequestDTO.getUserId(), UUID.randomUUID(), elasticSaveRequestDTO.getJsonData());
        } else {
            return sendAddRequestElastic(elasticSaveRequestDTO.getUserId(), elasticSaveRequestDTO.getId(), elasticSaveRequestDTO.getJsonData());
        }
    }

    private String sendAddRequestElastic(Long userId, UUID id, String jsonData) {
        IndexRequest indexRequest = new IndexRequest(indexPrefix + userId)
                .id(id.toString())
                .source(jsonData, XContentType.JSON);

        log.info("handling response from elastic");
        try {
            return elasticsearchClient.index(indexRequest, RequestOptions.DEFAULT).getId();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    private Map<String, Long> fillMap(ParsedStringTerms parsedStringTerms, String name) {
        Map<String, Long> map = new HashMap<>();

        parsedStringTerms
                .getBuckets()
                .forEach(bucket -> {
                    ParsedSum sumAggregation = bucket.getAggregations().get(name);
                    map.put(bucket.getKeyAsString(), (long) sumAggregation.getValue());
                });
        return map;
    }

    private String readFileAsString(URI file) throws IOException {
        return new String(Files.readAllBytes(Paths.get(file)));
    }

    private List<String> getIndices() {
        try {
            Request request = new Request("GET", "/_cat/indices?h=i");

            InputStream inputStream = elasticsearchClient.getLowLevelClient()
                    .performRequest(request)
                    .getEntity()
                    .getContent();

            return new BufferedReader(new InputStreamReader(inputStream))
                    .lines()
                    .filter(index -> index.startsWith(indexPrefix))
                    .toList();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}