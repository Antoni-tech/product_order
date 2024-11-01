package kz.symtech.antifraud.models.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChangePasswordRequest {

    private String code;

    @NotNull
    private String login;

    @NotNull
    private String oldPassword;

    @NotNull
    private String newPassword;
}
