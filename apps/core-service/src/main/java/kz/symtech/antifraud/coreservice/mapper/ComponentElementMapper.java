package kz.symtech.antifraud.coreservice.mapper;

import kz.symtech.antifraud.coreservice.entities.models.ModelComponentElements;
import kz.symtech.antifraud.models.dto.model.ComponentElementDTO;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class ComponentElementMapper implements Function<ModelComponentElements, ComponentElementDTO> {
    @Override
    public ComponentElementDTO apply(ModelComponentElements modelComponentElements) {
        return ComponentElementDTO.builder()
                .type(modelComponentElements.getType())
                .value(modelComponentElements.getValue())
                .enumField(modelComponentElements.getEnumField())
                .build();
    }
}
