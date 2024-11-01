package kz.symtech.antifraud.adminservice.dto.users;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateUserRequest {

    @NotNull
    private String login;

    @NotNull
    private String name;

    @NotNull
    private String phone;

    @Email
    private String email;

    @NotNull
    private List<Long> roleIds;

    private Boolean copyShippingToBilling;
}
