package kz.symtech.antifraud.coreservice.dto.filter.response;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductTableDTO {
    private Long id;
    private String name;
    private Long price;
}
