package itca.uz.ura_cashback_2.payload;

import itca.uz.ura_cashback_2.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Statistic {
    private Long id;
    private User admin;
    private User user;
    private int cash_price;
    private int cashback;
}
