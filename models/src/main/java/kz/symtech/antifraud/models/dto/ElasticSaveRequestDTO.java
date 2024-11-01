package kz.symtech.antifraud.models.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Builder
public class ElasticSaveRequestDTO {
    private Boolean saveResult;
    private Boolean increment;
    private Long userId;
    private UUID id;
    private String jsonData;
}
