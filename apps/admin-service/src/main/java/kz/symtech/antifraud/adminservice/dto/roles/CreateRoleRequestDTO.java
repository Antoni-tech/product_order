package kz.symtech.antifraud.adminservice.dto.roles;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class CreateRoleRequestDTO {
    private String name;

    private List<Long> privileges;
}
