package kz.symtech.antifraud.coreservice.dto.filter.response;

import kz.symtech.antifraud.coreservice.enums.SummaryDataType;
import lombok.*;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
public class AccessFieldDTO {
    private Long id;
    private Map<Integer, String> tags;
    private SummaryDataType type;
    private List<SummaryMiniFieldDTO> fields;
}
