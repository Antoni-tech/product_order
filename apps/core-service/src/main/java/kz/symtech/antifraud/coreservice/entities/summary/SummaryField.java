package kz.symtech.antifraud.coreservice.entities.summary;

import jakarta.persistence.*;
import kz.symtech.antifraud.models.entity.BaseEntity;
import kz.symtech.antifraud.models.enums.SummaryFieldType;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnTransformer;

@Entity
@Getter
@Setter
@Table(name = "summary_fields")
public class SummaryField extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "summary_data_version_id", referencedColumnName = "id")
    private SummaryDataVersion summaryDataVersion;

    private String name;

    @ManyToOne
    @JoinColumn(name = "parent_id")
    private SummaryField parentSummaryField;

    private Boolean defaultField = false;

    @Enumerated(EnumType.STRING)
    private SummaryFieldType fieldType;

    private Long maxArray;

    private Integer queue;

    @OneToOne(mappedBy = "summaryField", cascade = CascadeType.REMOVE)
    private SummaryFieldSubData summaryFieldSubData;

    @ColumnTransformer(write = "?::json")
    private String testValueJson;

    public void copyTo(SummaryField summaryField){
        summaryField.setName(getName());
        summaryField.setDefaultField(getDefaultField());
        summaryField.setFieldType(getFieldType());
        summaryField.setMaxArray(getMaxArray());
        summaryField.setQueue(getQueue());
    }
}