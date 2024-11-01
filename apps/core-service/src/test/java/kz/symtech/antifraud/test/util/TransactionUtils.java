package kz.symtech.antifraud.test.util;

import kz.symtech.antifraud.coreservice.dto.TransactionRequestDto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

public class TransactionUtils {

    public static List<TransactionRequestDto> generateList(int n) {
        List<TransactionRequestDto> transactionRequestDtoList = new ArrayList<>();
        IntStream
                .range(0, n)
                .forEach(x -> transactionRequestDtoList.add(TransactionRequestDto.builder()
                        .accCt(generateIban().toString())
                        .accDt(generateIban().toString())
                        .sumPay(new BigDecimal(generateRandomInt(1000, 100_000)))
                        .currency("KZT")
                        .build()));

        return transactionRequestDtoList;
    }

    public static StringBuilder generateIban() {
        return new StringBuilder()
                .append("KZ")
                .append(generateRandomInt(10_000, 90_000))
                .append("B")
                .append(generateRandomInt(100_000, 900_000))
                .append("KZT");
    }

    public static int generateRandomInt(int min, int max) {
        Random random = new Random();
        return min + random.nextInt(max);
    }
}
