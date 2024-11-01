package kz.symtech.antifraud.testservice.services;

import kz.symtech.antifraud.testservice.dto.TransactionDataRequestDto;

public interface CommonService {
    Long saveTransactionData(TransactionDataRequestDto transactionDataRequestDto);
}
