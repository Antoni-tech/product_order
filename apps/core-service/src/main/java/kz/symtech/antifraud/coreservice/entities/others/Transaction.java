package kz.symtech.antifraud.coreservice.entities.others;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "transactions")
@Getter
@Setter
public class Transaction {

    @Id
    @Column
    @GeneratedValue
    private UUID id;

    @Column
    private String accDt;

    @Column
    private String accCt;

    @Column
    private BigDecimal sumPay;

    @Column
    private String currency;

    @Column
    private String status;
}
