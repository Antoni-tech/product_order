package kz.symtech.antifraud.coreservice.dto.filter.response;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderTableDTO {
    private Long id;
    private Long total;
    private Long product_order_id;
    private Long tax;
}
