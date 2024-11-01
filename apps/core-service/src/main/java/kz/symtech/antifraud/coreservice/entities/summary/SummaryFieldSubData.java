package kz.symtech.antifraud.coreservice.entities.summary;

import jakarta.persistence.*;
import kz.symtech.antifraud.models.entity.BaseEntity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "summary_field_sub_data")
public class SummaryFieldSubData extends BaseEntity {

    private Integer maxSize;

    private Boolean allowEmpty;

    private Boolean prohibitSpecCharacters;

    private Boolean allowArray;

    @OneToOne
    @JoinColumn(name = "summary_field_id", referencedColumnName = "id")
    private SummaryField summaryField;

    public void copyTo(SummaryFieldSubData summaryFieldSubData){
        summaryFieldSubData.setMaxSize(getMaxSize());
        summaryFieldSubData.setAllowEmpty(getAllowEmpty());
        summaryFieldSubData.setProhibitSpecCharacters(getProhibitSpecCharacters());
        summaryFieldSubData.setAllowArray(getAllowArray());
    }
}
