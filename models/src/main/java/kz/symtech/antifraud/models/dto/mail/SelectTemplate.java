package kz.symtech.antifraud.models.dto.mail;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum SelectTemplate {
    REQUEST_USER_CREATION("request-user-creation.ftl", UserCreationRequest.class, false);

    final String template;
    final Class<?> requestClass;
    final Boolean recipientUser;
}
