package kz.symtech.antifraud.coreservice.dto;

import kz.symtech.antifraud.models.dto.model.ComponentModel;
import lombok.*;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ModelStructCacheDTO implements Serializable {
    private List<ComponentModel> componentModelList;
}
