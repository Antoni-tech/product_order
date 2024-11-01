package kz.symtech.antifraud.coreservice.dto;

import kz.symtech.antifraud.coreservice.enums.ExtendedType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ExtendedRequestDTO {
    private String id;
    private ExtendedType type;
}
