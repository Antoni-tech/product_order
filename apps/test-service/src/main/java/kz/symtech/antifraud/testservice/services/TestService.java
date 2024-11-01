package kz.symtech.antifraud.testservice.services;

import kz.symtech.antifraud.testservice.entities.TransactionData;

public interface TestService {
    void startStopTransaction(Long id);
    void send(TransactionData transactionData);
    void sendReplace(TransactionData transactionData);
    void execute(TransactionData transactionData);
    void executeReplace(TransactionData transactionData);
}
