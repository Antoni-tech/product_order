package kz.symtech.antifraud.coreservice.dto.filter.response;

import kz.symtech.antifraud.models.enums.SummaryFieldType;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SummaryFieldDTO {
    private Long id;
    private String name;
    private Boolean defaultField;
    private SummaryFieldType fieldType;
    private Long maxArray;
    private Long srcRelationId;
    private Object testValueJson;
}
