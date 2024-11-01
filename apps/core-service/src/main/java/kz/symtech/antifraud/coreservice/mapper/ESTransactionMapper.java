package kz.symtech.antifraud.coreservice.mapper;

import kz.symtech.antifraud.coreservice.entities.others.Transaction;
import kz.symtech.antifraud.coreservice.elastic.entities.ESTransaction;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class ESTransactionMapper implements Function<Transaction, ESTransaction> {
    @Override
    public ESTransaction apply(Transaction transaction) {
        return ESTransaction.builder()
                .transactionId(transaction.getId())
                .accDt(transaction.getAccDt())
                .accCt(transaction.getAccCt())
                .sumPay(transaction.getSumPay())
                .status(transaction.getStatus())
                .currency(transaction.getCurrency())
                .build();
    }
}
