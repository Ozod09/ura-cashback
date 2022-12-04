package itca.uz.ura_cashback_2.payload;

import itca.uz.ura_cashback_2.entity.Company;
import itca.uz.ura_cashback_2.entity.User;
import lombok.*;

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
    private int cashPrice;
    private int cashback;
    private String date;
    private Company company;

}
