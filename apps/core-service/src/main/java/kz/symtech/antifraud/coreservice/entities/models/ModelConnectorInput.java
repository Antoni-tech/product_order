package kz.symtech.antifraud.coreservice.entities.models;

import jakarta.persistence.*;
import kz.symtech.antifraud.coreservice.entities.summary.SummaryDataVersion;
import kz.symtech.antifraud.coreservice.enums.ConnectorProtocol;
import kz.symtech.antifraud.coreservice.enums.ConnectorSubType;
import kz.symtech.antifraud.coreservice.enums.InputDataType;
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
public class ModelConnectorInput extends BaseEntity {

    static final long serialVersionUID = 8365858055083911861L;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "summaryDataVersionId", referencedColumnName = "id")
    private SummaryDataVersion summaryDataVersion;

    private String connectorPurpose;

    private String connectorSubspecies;

    private String technology;

    private String dataFormat;

    private String urlForInfo;

    private String cron;

    @Column
    @Enumerated(EnumType.STRING)
    private SummarySubType summarySubType;

    @Column
    @Enumerated(EnumType.STRING)
    private InputDataType inputDataType;

    @Column
    @Enumerated(EnumType.STRING)
    private ConnectorProtocol connectorProtocol;

    @Column
    @Enumerated(EnumType.STRING)
    private ConnectorSubType connectorSubType;
}
