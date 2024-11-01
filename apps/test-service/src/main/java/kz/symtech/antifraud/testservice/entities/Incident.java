package kz.symtech.antifraud.testservice.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import kz.symtech.antifraud.models.entity.BaseEntity;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnTransformer;

@Entity
@Getter
@Setter
@Table(name = "incidents_test")
public class Incident extends BaseEntity {

    @Column
    private Long summaryDataVersionModelId;

    @Column
    private Long userId;

    @Column
    private Long summaryDataVersionConnectorId;

    @Column
    private Long summaryDataVersionConnectorOutputId;

    @Column
    private int number;

    @Column
    private String textValue;

    @ColumnTransformer(write = "?::json")
    private String jsonData;
}

