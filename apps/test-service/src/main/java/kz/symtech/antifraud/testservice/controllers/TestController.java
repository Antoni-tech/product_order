package kz.symtech.antifraud.testservice.controllers;

import jakarta.validation.Valid;
import kz.symtech.antifraud.testservice.dto.TransactionDataRequestDto;
import kz.symtech.antifraud.testservice.services.CommonService;
import kz.symtech.antifraud.testservice.services.TestService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
@Slf4j
@RequiredArgsConstructor
public class TestController {

    private final TestService testService;
    private final CommonService commonService;

    @PostMapping("/start-stop/{id}")
    public void startStopTransaction(@PathVariable Long id) {
        testService.startStopTransaction(id);
    }

    @PostMapping("/save-or-update")
    public Long saveOrUpdate(@RequestBody @Valid TransactionDataRequestDto transactionDataRequestDto) {
        return commonService.saveTransactionData(transactionDataRequestDto);
    }
}
