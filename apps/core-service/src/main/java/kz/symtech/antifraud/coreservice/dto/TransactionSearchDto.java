package kz.symtech.antifraud.coreservice.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TransactionSearchDto {
    private String accCt;
    private String transactionId;
    private int offset;
    private int pageSize;
}
