package kz.symtech.antifraud.coreservice.dto;

import com.fasterxml.jackson.databind.JsonNode;
import kz.symtech.antifraud.coreservice.enums.SummaryDataType;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class ModelRuleCreateRequestDTO {
    private Boolean isCreate;
    private String versionId;
    private SummaryDataType type;
    private String name;
    private String description;
    private Boolean validateFields;
    private Boolean saveResult;
    private String summarySubType;
    private int queueNumber;
    private int daysRemaining;
    private Boolean resultIncremental;
    private Boolean common;
    private JsonNode jsonData;
    private ExtendedRequestDTO extend;
    private List<Object> fields;
}
