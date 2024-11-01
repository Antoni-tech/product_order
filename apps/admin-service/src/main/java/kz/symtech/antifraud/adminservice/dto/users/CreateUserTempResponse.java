package kz.symtech.antifraud.adminservice.dto.users;

import kz.symtech.antifraud.adminservice.entities.users.Users;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateUserTempResponse {
    private Users user;
    private String password;
}
