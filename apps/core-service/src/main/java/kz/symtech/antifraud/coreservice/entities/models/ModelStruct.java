package kz.symtech.antifraud.coreservice.entities.models;

import jakarta.persistence.*;
import kz.symtech.antifraud.coreservice.entities.others.FieldRelation;
import kz.symtech.antifraud.coreservice.entities.summary.SummaryDataVersion;
import kz.symtech.antifraud.models.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Getter
@Setter
public class ModelStruct extends BaseEntity {

    @OneToOne
    @JoinColumn(name = "summaryDataVersionId", referencedColumnName = "id")
    private SummaryDataVersion summaryDataVersion;

    @OneToMany(mappedBy = "modelStruct", cascade = CascadeType.ALL)
    private List<ModelStructComponents> modelStructComponents;

    @OneToMany(mappedBy = "modelStruct", cascade = CascadeType.ALL)
    private List<FieldRelation> fieldRelations;

    @OneToMany(mappedBy = "modelStruct", cascade = CascadeType.ALL)
    private List<TransactionCounter> transactionCounters;
}