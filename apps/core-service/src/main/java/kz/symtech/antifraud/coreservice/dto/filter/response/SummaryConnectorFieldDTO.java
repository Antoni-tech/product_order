package kz.symtech.antifraud.coreservice.dto.filter.response;

import kz.symtech.antifraud.models.enums.SummaryFieldType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
public class SummaryConnectorFieldDTO extends SummaryFieldDTO {
    private SummaryFieldType fieldType;
    private Integer maxSize;
    private Boolean allowEmpty;
    private Boolean allowArray;
    private Long maxArray;
    private Boolean prohibitSpecCharacters;
    private List<SummaryConnectorFieldDTO> children;
}
