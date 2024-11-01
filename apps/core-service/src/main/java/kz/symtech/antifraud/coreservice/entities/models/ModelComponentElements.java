package kz.symtech.antifraud.coreservice.entities.models;

import jakarta.persistence.*;
import kz.symtech.antifraud.models.entity.BaseEntity;
import kz.symtech.antifraud.models.enums.ModelComponentEnumField;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Getter
@Setter
public class ModelComponentElements extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "model_struct_component_id", referencedColumnName = "id")
    private ModelStructComponents modelStructComponent;

    @Column
    @Enumerated(EnumType.STRING)
    private ModelComponentEnumField enumField;

    private String type;

    private String value;

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        ModelComponentElements modelComponentElement = (ModelComponentElements) obj;
        return Objects.equals(modelStructComponent, modelComponentElement.modelStructComponent);
    }

    @Override
    public int hashCode() {
        return Objects.hash(modelStructComponent);
    }
}