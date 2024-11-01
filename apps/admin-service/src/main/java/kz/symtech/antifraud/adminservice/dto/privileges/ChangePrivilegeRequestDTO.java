package kz.symtech.antifraud.adminservice.dto.privileges;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class ChangePrivilegeRequestDTO {

    private String name;

    private List<Long> privileges;
}
