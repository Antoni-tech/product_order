package kz.symtech.antifraud.authservice.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class LoginRequest {

    @NotNull
    private String login;

    @NotNull
    private String password;
}
