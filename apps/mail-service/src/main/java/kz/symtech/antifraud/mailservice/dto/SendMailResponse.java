package kz.symtech.antifraud.mailservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Данный класс представляет собой абстракцию над
 * данными ответа сервиса отправки почты
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SendMailResponse {
    private String status;
}
