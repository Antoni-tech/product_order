package kz.symtech.antifraud.models.dto.model;

import kz.symtech.antifraud.models.enums.ModelComponentEnumField;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ComponentElementDTO implements Serializable {
    private ModelComponentEnumField enumField;
    private String type;
    private String value;
}
