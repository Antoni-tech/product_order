package kz.symtech.antifraud.models.dto.model;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class TransactionCounterDTO implements Serializable {
    private Long id;
    private String state;
    private Long summaryDataVersionModelId;
    private Long summaryDataVersionId;
}
