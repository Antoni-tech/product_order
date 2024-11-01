package kz.symtech.antifraud.coreservice.services.impl;

import kz.symtech.antifraud.coreservice.dto.TransactionRequestDto;
import kz.symtech.antifraud.coreservice.dto.TransactionSearchDto;
import kz.symtech.antifraud.coreservice.entities.others.Transaction;
import kz.symtech.antifraud.coreservice.enums.StatusEnum;
import kz.symtech.antifraud.coreservice.elastic.entities.ESTransaction;
import kz.symtech.antifraud.coreservice.elastic.repository.ESTransactionRepository;
import kz.symtech.antifraud.coreservice.mapper.ESTransactionMapper;
import kz.symtech.antifraud.coreservice.mapper.TransactionMapper;
import kz.symtech.antifraud.coreservice.repository.TransactionRepository;
import kz.symtech.antifraud.coreservice.services.TransactionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Profile({"prod", "test"})
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final ESTransactionRepository esTransactionRepository;
    private final TransactionMapper transactionMapper;
    private final ESTransactionMapper esTransactionMapper;

    private static final BigDecimal MIN_SUM = BigDecimal.valueOf(10_000);

    @Override
    public Transaction handleRequests(TransactionRequestDto transactionRequestDto) {

        Transaction transaction = transactionMapper.apply(transactionRequestDto);

        if (transactionRequestDto.getSumPay().compareTo(MIN_SUM) > 0) {
            transaction.setStatus(StatusEnum.ACCEPTED.name());
        } else {
            transaction.setStatus(StatusEnum.DENIED.name());
        }

        transactionRepository.save(transaction);
        esTransactionRepository.save(esTransactionMapper.apply(transaction));

        log.info(transaction.getId() + " " + transaction.getStatus());
        return transaction;
    }

    @Override
    public List<SearchHit<ESTransaction>> getAll(TransactionSearchDto searchDto) {
        return esTransactionRepository
                .findByAccCtAndTransactionId(searchDto.getAccCt(), searchDto.getTransactionId(), PageRequest.of(searchDto.getOffset(), searchDto.getPageSize()));
    }
}
