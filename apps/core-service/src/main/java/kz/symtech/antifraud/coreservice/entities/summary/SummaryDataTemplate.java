package kz.symtech.antifraud.coreservice.entities.summary;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import kz.symtech.antifraud.models.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Getter
@Setter
public class SummaryDataTemplate extends BaseEntity {

    @OneToOne
    @JoinColumn(name = "summaryDataId", referencedColumnName = "id")
    private SummaryData summaryData;

    private UUID templateId;
}
