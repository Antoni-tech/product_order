package kz.symtech.antifraud.coreservice.services.impl;

import kz.symtech.antifraud.coreservice.services.CountDocumentService;
import kz.symtech.antifraud.models.enums.Fields;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.core.CountRequest;
import org.elasticsearch.client.core.CountResponse;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.ParsedStringTerms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static kz.symtech.antifraud.coreservice.services.impl.ElasticsearchServiceImpl.TERM_COUNT_BY_FIELD;

@Service
@Slf4j
@RequiredArgsConstructor
public class CountDocumentServiceImpl implements CountDocumentService {

    @Value("${index-prefixes.user}")
    private String indexPrefix;

    private final RestHighLevelClient elasticsearchClient;

    @Override
    public Map<String, Long> countDocumentsById(Long userId, String fieldName) {
        String indexName = indexPrefix + userId;
        SearchRequest searchRequest = new SearchRequest(indexName);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.size(0);

        TermsAggregationBuilder aggregationBuilder = AggregationBuilders
                .terms(TERM_COUNT_BY_FIELD + fieldName)
                .field(fieldName);
        searchSourceBuilder.aggregation(aggregationBuilder);

        searchRequest.source(searchSourceBuilder);

        SearchResponse searchResponse;
        Map<String, Long> result = new HashMap<>();

        try {
            searchResponse = elasticsearchClient.search(searchRequest, RequestOptions.DEFAULT);
            ParsedStringTerms countByFieldAggregation = searchResponse.getAggregations().get(TERM_COUNT_BY_FIELD + fieldName);
            countByFieldAggregation.getBuckets().forEach(bucket -> result.put(bucket.getKeyAsString(), bucket.getDocCount()));
        } catch (IOException e) {
            log.error(e.getMessage());
        }

        return result;
    }

    @Override
    public Long countDocumentsByRuleId(Long userId, Long ruleId) {
        String indexName = indexPrefix + userId;
        try {

            CountRequest countRequest = new CountRequest(indexName);
            SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
            searchSourceBuilder.query(QueryBuilders.termQuery(Fields.RULE_ID_FIELD, ruleId));
            countRequest.source(searchSourceBuilder);

            CountResponse countResponse = elasticsearchClient.count(countRequest, RequestOptions.DEFAULT);
            return countResponse.getCount();
        } catch (IOException e) {
            log.error(e.getMessage());
            return -1L;
        }
    }
}
