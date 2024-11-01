package kz.symtech.antifraud.mailservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Данный класс представляет собой абстракцию
 * над входящими данными запроса
 * на отправку письма
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SendMailRequest {
    private String email;

    private String letterSubject;

    private String letter;
}
