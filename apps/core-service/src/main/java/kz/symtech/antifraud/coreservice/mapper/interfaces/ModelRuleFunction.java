package kz.symtech.antifraud.coreservice.mapper.interfaces;

import kz.symtech.antifraud.coreservice.entities.models.ModelRule;
import kz.symtech.antifraud.models.dto.model.ComponentModel;
import kz.symtech.antifraud.models.dto.model.ModelRuleCacheDTO;

@FunctionalInterface
public interface ModelRuleFunction {
    ModelRuleCacheDTO apply(ModelRule modelRule, ComponentModel componentModel);
}
