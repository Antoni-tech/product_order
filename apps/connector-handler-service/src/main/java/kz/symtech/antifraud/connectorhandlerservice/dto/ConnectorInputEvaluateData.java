package kz.symtech.antifraud.connectorhandlerservice.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Map;

@Getter
@Setter
@Builder
@ToString
public class ConnectorInputEvaluateData {
    private Map<String, Object> prevMap;
    private Map<String, Object> map;
}
