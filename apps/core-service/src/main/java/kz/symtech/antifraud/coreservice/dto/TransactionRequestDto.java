package kz.symtech.antifraud.coreservice.dto;

import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
public class TransactionRequestDto implements Serializable {
    private String accDt;
    private String accCt;
    private BigDecimal sumPay;
    private String currency;
}
