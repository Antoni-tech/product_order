package kz.symtech.antifraud.coreservice.mapper;

import kz.symtech.antifraud.coreservice.dto.TransactionRequestDto;
import kz.symtech.antifraud.coreservice.entities.others.Transaction;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class TransactionMapper implements Function<TransactionRequestDto, Transaction> {
    @Override
    public Transaction apply(TransactionRequestDto transactionRequestDto) {
        Transaction transaction = new Transaction();
        transaction.setAccDt(transactionRequestDto.getAccDt());
        transaction.setAccCt(transactionRequestDto.getAccCt());
        transaction.setCurrency(transactionRequestDto.getCurrency());
        transaction.setSumPay(transactionRequestDto.getSumPay());

        return transaction;
    }
}
