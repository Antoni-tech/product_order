package kz.symtech.antifraud.models.dto.model;


import kz.symtech.antifraud.models.enums.SummaryFieldType;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SummaryFieldCacheDTO implements Serializable {
    private Long id;
    private Long summaryDataVersionId;
    private String name;
    private SummaryFieldType type;
    private Boolean defaultField;
    private SummaryFieldCacheDTO parentSummaryField;
    private SummaryFieldSubDataDTO summaryFieldSubDataDTO;
}
