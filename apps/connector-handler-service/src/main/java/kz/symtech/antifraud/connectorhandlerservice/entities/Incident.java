package kz.symtech.antifraud.connectorhandlerservice.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnTransformer;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "incidents")
@Getter
@Setter
public class Incident {
    @Id
    @Column
    @GeneratedValue
    private UUID id;

    @Column(updatable = false)
    @CreationTimestamp
    private Date createdAt;

    @UpdateTimestamp
    private Date updatedAt;

    @Column
    private int number;

    @Column
    private String textValue;

    @ColumnTransformer(write = "?::json")
    private String jsonData;

    @Column
    private Long summaryDataVersionModelId;

    @Column
    private Long summaryDataVersionConnectorId;

    @Column
    private Long userId;
}
