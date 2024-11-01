package kz.symtech.antifraud.coreservice.services;

import kz.symtech.antifraud.coreservice.dto.TransactionRequestDto;
import kz.symtech.antifraud.coreservice.dto.TransactionSearchDto;
import kz.symtech.antifraud.coreservice.entities.others.Transaction;
import kz.symtech.antifraud.coreservice.elastic.entities.ESTransaction;
import org.springframework.data.elasticsearch.core.SearchHit;

import java.util.List;

public interface TransactionService {
    Transaction handleRequests(TransactionRequestDto transactionRequestDto);
    List<SearchHit<ESTransaction>> getAll(TransactionSearchDto searchDto);

}
