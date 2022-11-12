package itca.uz.ura_cashback_2.payload;

import itca.uz.ura_cashback_2.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class OrderDto {
    private Long id;
    private Long createdBy;
    private Long adminId;
    private User admin;
    private Long clientId;
    private User client;
    private int cash_price;
    private int cashback;

}
