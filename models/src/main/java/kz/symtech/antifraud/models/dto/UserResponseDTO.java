package kz.symtech.antifraud.models.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.Set;


/**
 * Данный класс представляет собой абстракцию
 * над данными пользователя в системе ТФ
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class UserResponseDTO {
    private long id;

    private String login;

    private String company;

    private String phone;

    private String email;

    private String salt;

    private String password;


    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Date lastLogin;

    @JsonProperty("roles")
    private Set<RoleResponseDTO.RoleData> role;
}
