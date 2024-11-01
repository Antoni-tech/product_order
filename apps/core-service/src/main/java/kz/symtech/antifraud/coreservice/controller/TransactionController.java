package kz.symtech.antifraud.coreservice.controller;

import kz.symtech.antifraud.coreservice.dto.TransactionRequestDto;
import kz.symtech.antifraud.coreservice.dto.TransactionSearchDto;
import kz.symtech.antifraud.coreservice.entities.others.Transaction;
import kz.symtech.antifraud.coreservice.elastic.entities.ESTransaction;
import kz.symtech.antifraud.coreservice.services.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/transactions")
@Profile({"prod", "test"})
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping
    public ResponseEntity<Transaction> handleRequests(@RequestBody TransactionRequestDto transactionRequestDto) {
        return ResponseEntity.ok(transactionService.handleRequests(transactionRequestDto));
    }

    @GetMapping
    public List<SearchHit<ESTransaction>> getAll(TransactionSearchDto searchDto) {
        return transactionService.getAll(searchDto);
    }
}
