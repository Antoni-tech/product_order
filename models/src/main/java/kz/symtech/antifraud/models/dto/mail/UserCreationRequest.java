package kz.symtech.antifraud.models.dto.mail;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserCreationRequest {

    private String name;
    private String company;
    private String email;
    private String phone;
}
