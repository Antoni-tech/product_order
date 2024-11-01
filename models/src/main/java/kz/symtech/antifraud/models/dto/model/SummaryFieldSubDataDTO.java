package kz.symtech.antifraud.models.dto.model;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SummaryFieldSubDataDTO implements Serializable {
    private Integer maxSize;
    private Boolean allowEmpty;
    private Boolean prohibitSpecCharacters;
    private Boolean allowArray;
}
