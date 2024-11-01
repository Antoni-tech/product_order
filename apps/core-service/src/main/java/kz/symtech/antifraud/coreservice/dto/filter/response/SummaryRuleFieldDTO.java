package kz.symtech.antifraud.coreservice.dto.filter.response;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
public class SummaryRuleFieldDTO extends SummaryFieldDTO {
    private List<SummaryRuleFieldDTO> children;
}
