package kz.symtech.antifraud.coreservice.dto.model.struct.request;

import kz.symtech.antifraud.coreservice.enums.SummaryDataType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ModelStructRequestDTO {
    private Boolean isCreate;
    private String versionId;
    private SummaryDataType type;
    private String name;
    private String description;
    private Boolean common;
    private List<ModelConnectorInputRequestDTO> inputs;
    private List<ModelConnectorOutputRequestDTO> outputs;
    private List<ModelRuleRequestDTO> rules;
}