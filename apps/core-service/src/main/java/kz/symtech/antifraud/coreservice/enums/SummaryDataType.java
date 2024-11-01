package kz.symtech.antifraud.coreservice.enums;

import kz.symtech.antifraud.coreservice.dto.filter.response.SummaryConnectorFieldDTO;
import kz.symtech.antifraud.coreservice.dto.filter.response.DataStructureFieldDTO;
import kz.symtech.antifraud.coreservice.dto.filter.response.SummaryFieldDTO;
import kz.symtech.antifraud.coreservice.dto.filter.response.SummaryRuleFieldDTO;
import kz.symtech.antifraud.coreservice.dto.model.struct.request.ModelStructComponentsRequestDTO;
import kz.symtech.antifraud.coreservice.dto.model.struct.request.ModelStructRequestDTO;
import kz.symtech.antifraud.coreservice.entities.models.ModelConnectorInput;
import kz.symtech.antifraud.coreservice.entities.models.ModelConnectorOutput;
import kz.symtech.antifraud.coreservice.entities.models.ModelRule;
import kz.symtech.antifraud.coreservice.entities.models.ModelStruct;
import kz.symtech.antifraud.coreservice.entities.summary.SummaryDataVersion;
import kz.symtech.antifraud.coreservice.repository.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collections;
import java.util.List;

@Getter
@AllArgsConstructor
public enum SummaryDataType {
    CONNECTOR_INPUT("input", ModelConnectorInput.class, ModelConnectorInputRepository.class, SummaryConnectorFieldDTO.class),
    CONNECTOR_OUTPUT("output", ModelConnectorOutput.class, ModelConnectorOutputRepository.class, SummaryConnectorFieldDTO.class),
    RULE("rule", ModelRule.class, ModelRuleRepository.class, SummaryRuleFieldDTO.class),
    MODEL("model", ModelStruct.class, ModelStructRepository.class, SummaryFieldDTO.class),
    DATA_STRUCTURE("data_structure", SummaryDataVersion.class, SummaryDataVersionRepository.class, DataStructureFieldDTO.class);

    private final String error;
    private final Class<?> aClass;
    private final Class<? extends JpaRepository<?, Long>> repository;
    private final Class<? extends SummaryFieldDTO> dtoType;

    public List<? extends ModelStructComponentsRequestDTO> getComponentListFromDTO(ModelStructRequestDTO modelStructRequestDTO) {
        return switch (this) {
            case CONNECTOR_INPUT -> modelStructRequestDTO.getInputs();
            case CONNECTOR_OUTPUT -> modelStructRequestDTO.getOutputs();
            case RULE -> modelStructRequestDTO.getRules();
            case MODEL, DATA_STRUCTURE -> Collections.emptyList();
        };
    }
}
