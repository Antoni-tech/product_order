package kz.symtech.antifraud.coreservice.entities.models;

import jakarta.persistence.*;
import kz.symtech.antifraud.coreservice.entities.summary.SummaryDataVersion;
import kz.symtech.antifraud.models.entity.BaseEntity;
import kz.symtech.antifraud.models.enums.SummarySubType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@AllArgsConstructor
@NoArgsConstructor
@Entity
@Getter
@Setter
public class ModelRule extends BaseEntity {

    @OneToOne
    @JoinColumn(name = "summaryDataVersionId", referencedColumnName = "id")
    private SummaryDataVersion summaryDataVersion;

    @Column(updatable = false)
    @Enumerated(EnumType.STRING)
    private SummarySubType summarySubType;

}