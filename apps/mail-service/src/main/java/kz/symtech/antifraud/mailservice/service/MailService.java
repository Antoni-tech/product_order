package kz.symtech.antifraud.mailservice.service;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import kz.symtech.antifraud.mailservice.config.CompanyConfig;
import kz.symtech.antifraud.mailservice.dto.SendMailRequest;
import kz.symtech.antifraud.mailservice.dto.SendMailResponse;
import kz.symtech.antifraud.models.dto.mail.SelectTemplate;
import kz.symtech.antifraud.models.dto.mail.UserCreationRequest;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.core.io.InputStreamSource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Данный класс реализует логику отправки писем пользователям
 * системы ТФ
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class MailService {

    private final MailProperties mailProperties;
    private final JavaMailSender javaMailSender;
    private final ResourceLoader resourceLoader;
    private final CompanyConfig companyConfig;
    private final Configuration freemarkerConfig;

    private String generateBody(Map<String, Object> model, String templateSrc) throws IOException, TemplateException {
        Template template = freemarkerConfig.getTemplate(templateSrc);
        return FreeMarkerTemplateUtils.processTemplateIntoString(template, model);
    }

    public SendMailResponse sendEmail(SendMailRequest sendMailRequest) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(sendMailRequest.getEmail());
        message.setFrom(mailProperties.getUsername());
        message.setSubject(sendMailRequest.getLetterSubject());
        message.setText(sendMailRequest.getLetter());
        javaMailSender.send(message);
        return SendMailResponse.builder()
                .status("SUCCESS")
                .build();
    }

    private SendMailResponse sendMail(String recipient, String title, Boolean logo, String htmlContent)
            throws IOException, MessagingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setTo(recipient);
        helper.setSubject(title);
        helper.setFrom(mailProperties.getUsername(), companyConfig.getName());
        helper.setText(htmlContent, true);

        if (logo) {
            InputStreamSource imageSource = () -> resourceLoader.getResource("classpath:/images/logo.png").getInputStream();
            helper.addInline("imageLogo", imageSource, "image/png");
        }

        javaMailSender.send(message);
        return SendMailResponse.builder()
                .status("SUCCESS")
                .build();
    }

    @SneakyThrows
    public SendMailResponse sendMailtoUsers(UserCreationRequest request, SelectTemplate emailTemplate) {
        HashMap<String, Object> model = new HashMap<>();
        String title = "Request creation User";
        model.put("name", request.getName());
        model.put("company", request.getCompany());
        model.put("email", request.getEmail());
        model.put("phone", request.getPhone());
        model.put("companyName", companyConfig.getName());
        model.put("companyEmail", companyConfig.getEmail());
        model.put("companyWebsite", companyConfig.getWebSite());
        String htmlContent = generateBody(model, emailTemplate.getTemplate());
        log.info("send email to {}", request.getEmail());
        return sendMail(request.getEmail(), title, true, htmlContent);
    }
}
