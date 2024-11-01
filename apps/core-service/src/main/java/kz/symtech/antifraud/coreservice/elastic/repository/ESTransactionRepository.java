package kz.symtech.antifraud.coreservice.elastic.repository;

import kz.symtech.antifraud.coreservice.elastic.entities.ESTransaction;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Highlight;
import org.springframework.data.elasticsearch.annotations.HighlightField;
import org.springframework.data.elasticsearch.annotations.HighlightParameters;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
@Profile({"prod", "test"})
public interface ESTransactionRepository extends ElasticsearchRepository<ESTransaction, String> {
    @Query("{\"bool\": {\"must\": [{\"match\": {\"accCt\": {\"query\": \"?0\"}}}, {\"match\": {\"transactionId\": {\"query\": \"?1\"}}}]}}")
    @Highlight(
            fields = {
                    @HighlightField(name = "accCt"),
                    @HighlightField(name = "transactionId")
            },
            parameters = @HighlightParameters(
                    preTags = "<mark>",
                    postTags = "</mark>",
                    fragmentSize = 500,
                    numberOfFragments = 3
            )
    )
    List<SearchHit<ESTransaction>> findByAccCtAndTransactionId(String accCt, String transactionId, Pageable pageable);
}
