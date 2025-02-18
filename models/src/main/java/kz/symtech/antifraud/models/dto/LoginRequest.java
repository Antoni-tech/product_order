package kz.symtech.antifraud.models.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class LoginRequest {

    @NotNull
    private String login;

    @NotNull
    private String password;
}
