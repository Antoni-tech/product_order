package kz.symtech.antifraud.testservice.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import kz.symtech.antifraud.models.entity.BaseEntity;
import kz.symtech.antifraud.testservice.enums.State;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.ColumnTransformer;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@Entity
@Getter
@Setter
@ToString
public class TransactionData extends BaseEntity {

    @Column
    private UUID summaryDataConnectorId;

    @Column
    private UUID summaryDataModelId;

    @Column
    private Long userId;

    @ColumnTransformer(write = "?::json")
    private String data;

    @ColumnTransformer(write = "?::json")
    private String dataToReplace;

    @Column
    private int numberOfRepetitions;

    @Column
    private Boolean isCyclical;

    @Column
    @Enumerated(EnumType.STRING)
    private State state = State.NOT_STARTED;

    @Column
    private int delay;

    @Column
    private int delayToReplace;

    @Column
    @Enumerated(EnumType.STRING)
    private ChronoUnit chronoUnit;

    @Column
    @Enumerated(EnumType.STRING)
    private ChronoUnit chronoUnitToReplace;

    @Column
    private LocalDateTime expiresAt;
}
