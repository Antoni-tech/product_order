package kz.symtech.antifraud.coreservice.entities.summary;

import jakarta.persistence.*;
import kz.symtech.antifraud.coreservice.entities.rules.RuleExpression;
import kz.symtech.antifraud.coreservice.enums.SummaryVersionState;
import kz.symtech.antifraud.models.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Getter
@Setter
public class SummaryDataVersion extends BaseEntity {
    @ManyToOne
    @JoinColumn(name = "summaryDataId", referencedColumnName = "id")
    private SummaryData summaryData;

    private Short version;

    private Boolean isTemplate;

    private Long userUpdateId;

    private Boolean isActive;

    private Boolean common;

    private Boolean validateFields;

    @Column(updatable = false, nullable = false)
    @CreationTimestamp
    private Date createDate;

    @Column(nullable = false)
    @UpdateTimestamp
    private Date updateDate;

    @Column
    @Enumerated(EnumType.STRING)
    private SummaryVersionState state;

    private String name;

    private String description;

    private Long extended;

    @OneToMany(mappedBy = "summaryDataVersion", cascade = CascadeType.REMOVE)
    private List<SummaryField> summaryFields;

    @OneToMany(mappedBy = "summaryDataVersionRule", cascade = CascadeType.ALL)
    private List<RuleExpression> ruleExpressions;
}