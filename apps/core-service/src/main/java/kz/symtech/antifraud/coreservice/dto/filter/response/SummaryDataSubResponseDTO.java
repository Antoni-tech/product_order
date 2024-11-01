package kz.symtech.antifraud.coreservice.dto.filter.response;

import kz.symtech.antifraud.models.enums.SummarySubType;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SummaryDataSubResponseDTO extends SummaryDataResponseDTO {
    private SummarySubType summarySubType;
    private List<ModelStructComponentsFilterResponseDTO> models;
    private List<? extends SummaryFieldDTO> fields;
}