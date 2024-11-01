package kz.symtech.antifraud.connectorhandlerservice.entities;

import jakarta.persistence.*;
import kz.symtech.antifraud.connectorhandlerservice.enums.TransactionInputDataType;
import kz.symtech.antifraud.models.entity.BaseEntity;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnTransformer;

@Entity
@Table(name = "transaction_input_data")
@Getter
@Setter
public class TransactionInputData extends BaseEntity {

    @Column
    private Long summaryDataVersionModelId;

    @Column
    private Long summaryDataVersionConnectorId;

    @Column
    private Long summaryDataVersionRuleId;

    @ColumnTransformer(write = "?::json")
    private String jsonData;

    @Enumerated(EnumType.STRING)
    private TransactionInputDataType type;
}
