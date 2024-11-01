package kz.symtech.antifraud.connectorhandlerservice.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@Builder
public class RuleInfo {
    private String condition;
    private String operation;
    private Map<String, Object> data;
    private List<FieldRelationSubInfoDTO> fieldRelationSubInfoDTOS;
}
