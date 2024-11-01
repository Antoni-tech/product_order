package kz.symtech.antifraud.testservice.controllers;

import kz.symtech.antifraud.testservice.entities.TransactionData;
import kz.symtech.antifraud.testservice.enums.State;
import kz.symtech.antifraud.testservice.services.TransactionDataService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/transaction-data")
@RequiredArgsConstructor
public class TransactionDataController {

    private final TransactionDataService transactionDataService;

    @GetMapping("/all")
    public List<TransactionData> getAll() {
        return transactionDataService.getAll();
    }

    @GetMapping("/{id}")
    public TransactionData get(@PathVariable Long id) {
        return transactionDataService.get(id);
    }

    @GetMapping("/get-all-by-state")
    public List<TransactionData> getByState(@RequestParam State state) {
        return transactionDataService.getAllByState(state);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        transactionDataService.delete(id);
    }
}
