package kz.symtech.antifraud.coreservice.entities.models;

import jakarta.persistence.*;
import kz.symtech.antifraud.coreservice.entities.summary.SummaryDataVersion;
import kz.symtech.antifraud.models.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Getter
@Setter
public class ModelStructComponents extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "model_struct_id", referencedColumnName = "id")
    private ModelStruct modelStruct;

    @ManyToOne
    @JoinColumn(name = "model_component_id", referencedColumnName = "id")
    private SummaryDataVersion summaryDataVersion;

    @OneToMany(mappedBy = "modelStructComponent", cascade = CascadeType.REMOVE)
    private List<ModelComponentElements> modelComponentElements = new ArrayList<>();

    private Integer daysRemaining;

    private Boolean resultIncremental;
}