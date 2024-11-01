package kz.symtech.antifraud.models.dto.model;

import kz.symtech.antifraud.models.enums.SummaryFieldType;
import kz.symtech.antifraud.models.enums.SummaryType;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class FieldRelationDTO implements Serializable {
    private String fieldNameVar;
    private String fieldNameSrc;
    private Boolean defaultField;
    private SummaryFieldType type;
    private Long summaryFieldId;
    private Long summaryDataVersionId;
    private Long sDVSourceId;
    private Long sourceId;
    private Long summaryDataVersionModelId;
    private SummaryType sourceType;
}
