package kz.symtech.antifraud.coreservice.mapper;

import kz.symtech.antifraud.coreservice.dto.field.relation.FieldRelationResponseDTO;
import kz.symtech.antifraud.coreservice.entities.others.FieldRelation;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class FieldRelationResponseMapper implements Function<FieldRelation, FieldRelationResponseDTO> {
    @Override
    public FieldRelationResponseDTO apply(FieldRelation f) {
        return FieldRelationResponseDTO
                .builder()
                .fieldRelationId(f.getId())
                .varSummaryFieldId(f.getVarSummaryField().getId())
                .varName(f.getVarSummaryField().getName())
                .srcSummaryFieldId(f.getSrcSummaryField().getId())
                .srcName(f.getSrcSummaryField().getName())
                .build();
    }
}
