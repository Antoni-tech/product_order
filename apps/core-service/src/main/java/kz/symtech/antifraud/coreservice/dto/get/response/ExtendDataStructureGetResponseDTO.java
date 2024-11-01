package kz.symtech.antifraud.coreservice.dto.get.response;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ExtendDataStructureGetResponseDTO {
    private Long extendedVersionId;
    private Boolean isActive;
}
