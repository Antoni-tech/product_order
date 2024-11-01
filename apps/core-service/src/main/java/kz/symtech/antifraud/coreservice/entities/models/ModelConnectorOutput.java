package kz.symtech.antifraud.coreservice.entities.models;

import jakarta.persistence.*;
import kz.symtech.antifraud.coreservice.entities.summary.SummaryDataVersion;
import kz.symtech.antifraud.coreservice.enums.ConnectorProtocol;
import kz.symtech.antifraud.coreservice.enums.ConnectorSubType;
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
public class ModelConnectorOutput extends BaseEntity {
    @OneToOne
    @JoinColumn(name = "summaryDataVersionId", referencedColumnName = "id")
    private SummaryDataVersion summaryDataVersion;

    private Boolean checkMainAttributes;

    private String dataFormat;

    @Column
    @Enumerated(EnumType.STRING)
    private SummarySubType summarySubType;

    private String url;

    private String urlForInfo;

    private String cron;

    @Column
    @Enumerated(EnumType.STRING)
    private ConnectorProtocol connectorProtocol;

    @Column
    @Enumerated(EnumType.STRING)
    private ConnectorSubType connectorSubType;
}