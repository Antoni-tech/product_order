package kz.symtech.antifraud.coreservice.elastic.entities;

import jakarta.persistence.Id;
import kz.symtech.antifraud.coreservice.helper.Indices;
import lombok.*;
import org.springframework.data.elasticsearch.annotations.*;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Mapping(mappingPath = "/mappings/elasticsearch-mappings.json")
@Setting(settingPath = "/settings/elasticsearch-settings.json")
@Document(indexName = Indices.TRANSACTION_INDEX)
public class ESTransaction {

    @Id
    private String id;
    private UUID transactionId;
    private String accDt;
    private String accCt;
    private BigDecimal sumPay;
    private String currency;
    private String status;
}
