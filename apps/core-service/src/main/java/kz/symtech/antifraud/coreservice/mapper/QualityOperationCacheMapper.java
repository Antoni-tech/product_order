package kz.symtech.antifraud.coreservice.mapper;

import kz.symtech.antifraud.coreservice.entities.rules.QualityOperation;
import kz.symtech.antifraud.coreservice.mapper.interfaces.QualityOperationFunction;
import kz.symtech.antifraud.models.dto.model.ComponentElementDTO;
import kz.symtech.antifraud.models.dto.model.ComponentModel;
import kz.symtech.antifraud.models.dto.model.QualityOperationDTO;
import kz.symtech.antifraud.models.enums.ModelComponentEnumField;
import org.springframework.stereotype.Service;

import java.util.Objects;

import static kz.symtech.antifraud.models.utils.ObjectMapperUtils.objectMapper;

@Service
public class QualityOperationCacheMapper implements QualityOperationFunction {

    @Override
    public QualityOperationDTO apply(QualityOperation qualityOperation, ComponentModel componentModel) {
        Long sDVConnectorId = getComponentElement(componentModel, ModelComponentEnumField.CONNECTOR_OUTPUT_SDV_ID);
        Boolean toIncidents = getComponentElement(componentModel, ModelComponentEnumField.TO_INCIDENTS);

        return QualityOperationDTO
                .builder()
                .number(qualityOperation.getNumber())
                .textValue(qualityOperation.getTextValue())
                .toIncidents(toIncidents)
                .summaryDataVersionConnectorOutputId(sDVConnectorId)
                .build();
    }

    @SuppressWarnings("unchecked")
    public <T> T getComponentElement(ComponentModel componentModel, ModelComponentEnumField enumField) {
        if (Objects.isNull(componentModel.getElements())) {
            return null;
        }

        ComponentElementDTO componentElementDTO = componentModel.getElements().stream()
                .filter(v -> v.getEnumField().equals(enumField))
                .findAny()
                .orElse(null);

        if (Objects.nonNull(componentElementDTO)) {
            Class<?> fieldType = componentElementDTO.getEnumField().getType();
            return objectMapper.convertValue(componentElementDTO.getValue(), (Class<T>) fieldType);
        }
        return null;
    }
}
