package kz.symtech.antifraud.coreservice.mapper;

import kz.symtech.antifraud.coreservice.dto.ModelConnectorInputDTO;
import kz.symtech.antifraud.coreservice.entities.models.ModelConnectorInput;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ModelConnectorInputDTOMapper {
    public ModelConnectorInputDTO buildModelConnectorInputDTO(ModelConnectorInput modelConnectorInput) {
        return ModelConnectorInputDTO.builder()
                .id(modelConnectorInput.getSummaryDataVersion().getId())
                .type(modelConnectorInput.getSummaryDataVersion().getSummaryData().getType())
                .name(modelConnectorInput.getSummaryDataVersion().getName())
                .createUserId(modelConnectorInput.getSummaryDataVersion().getUserUpdateId())
                .technology(modelConnectorInput.getTechnology())
                .dataFormat(modelConnectorInput.getDataFormat())
                .inputDataType(modelConnectorInput.getInputDataType())
                .build();
    }
}
