package kz.symtech.antifraud.mailservice.controller;

import kz.symtech.antifraud.mailservice.dto.SendMailRequest;
import kz.symtech.antifraud.mailservice.dto.SendMailResponse;
import kz.symtech.antifraud.mailservice.service.MailService;
import freemarker.template.TemplateException;
import jakarta.mail.MessagingException;
import kz.symtech.antifraud.models.dto.mail.SelectTemplate;
import kz.symtech.antifraud.models.dto.mail.UserCreationRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

/**
 * Данный контроллер обрабатывает входящие запросы
 * на отправку писем пользователям после регистрации нового пользователя в сети
 */
@RestController
@RequestMapping("/mail")
@RequiredArgsConstructor
public class MailController {

    private final MailService mailService;

    @PostMapping("/send")
    public ResponseEntity<SendMailResponse> sendMail(@RequestBody SendMailRequest sendMailRequest) {
        SendMailResponse result = mailService.sendEmail(sendMailRequest);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/req-usr-create")
    public ResponseEntity<SendMailResponse> requestUserCreation(@RequestBody UserCreationRequest request) throws TemplateException, IOException, MessagingException {
        SendMailResponse result = mailService.sendMailtoUsers(request, SelectTemplate.REQUEST_USER_CREATION);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}