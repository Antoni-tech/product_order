package kz.symtech.antifraud.coreservice.entities.models;

import jakarta.persistence.*;
import kz.symtech.antifraud.coreservice.entities.summary.SummaryDataVersion;
import kz.symtech.antifraud.models.entity.BaseEntity;
import kz.symtech.antifraud.models.enums.TransactionCounterState;
import lombok.Getter;
import lombok.Setter;


@Entity
@Getter
@Setter
@Table(name = "transaction_counter")
public class TransactionCounter extends BaseEntity {

    @Column
    private int amountOfTransactions;

    @Column
    private int amountOfErrors;

    @Column
    @Enumerated(EnumType.STRING)
    private TransactionCounterState state;

    @ManyToOne
    @JoinColumn(name = "summary_data_version_id", referencedColumnName = "id")
    private SummaryDataVersion summaryDataVersion;

    @ManyToOne
    @JoinColumn(name = "model_id", referencedColumnName = "id")
    private ModelStruct modelStruct;

    @Column
    private String lastError;

    public void incrementErrors(int delta) {
        amountOfErrors += delta;
    }

    public void incrementTransactionsAmount() {
        amountOfTransactions++;
    }
}
