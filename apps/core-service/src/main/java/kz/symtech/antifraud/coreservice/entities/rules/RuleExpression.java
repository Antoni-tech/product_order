package kz.symtech.antifraud.coreservice.entities.rules;

import jakarta.persistence.*;
import kz.symtech.antifraud.coreservice.entities.summary.SummaryDataVersion;
import kz.symtech.antifraud.models.entity.BaseEntity;
import kz.symtech.antifraud.models.enums.ExpressionType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class RuleExpression extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "summary_data_version_rule_id", referencedColumnName = "id")
    private SummaryDataVersion summaryDataVersionRule;

    @Column
    private String expression;

    @Enumerated(EnumType.STRING)
    private ExpressionType type;

    @OneToOne(mappedBy = "ruleExpression", cascade = CascadeType.ALL)
    private QualityOperation qualityOperation;
}
