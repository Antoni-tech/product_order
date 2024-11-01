package kz.symtech.antifraud.coreservice.dto.get.response;

import kz.symtech.antifraud.coreservice.dto.filter.response.ModelStructComponentsDTO;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class ModelStructGetResponseDTO {
    private Long summaryDataVersionId;
    private Long userId;
    private String userName;
    private Long modelId;
    private String modelName;
    private List<ModelStructComponentsDTO> components;
}
