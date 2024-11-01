package kz.symtech.antifraud.coreservice.entities.others;

import jakarta.persistence.*;
import kz.symtech.antifraud.coreservice.entities.models.ModelStruct;
import kz.symtech.antifraud.coreservice.entities.summary.SummaryField;
import kz.symtech.antifraud.models.entity.BaseEntity;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "field_relation")
public class FieldRelation extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "model_struct_id", referencedColumnName = "id")
    private ModelStruct modelStruct;

    @ManyToOne
    @JoinColumn(name = "var_summary_field_id", referencedColumnName = "id")
    private SummaryField varSummaryField;

    @ManyToOne
    @JoinColumn(name = "src_summary_field_id", referencedColumnName = "id")
    private SummaryField srcSummaryField;
}
