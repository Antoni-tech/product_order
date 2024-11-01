package kz.symtech.antifraud.coreservice.mapper.interfaces;

import kz.symtech.antifraud.coreservice.entities.rules.QualityOperation;
import kz.symtech.antifraud.models.dto.model.ComponentModel;
import kz.symtech.antifraud.models.dto.model.QualityOperationDTO;

@FunctionalInterface
public interface QualityOperationFunction {
    QualityOperationDTO apply(QualityOperation qualityOperation, ComponentModel componentModel);
}
