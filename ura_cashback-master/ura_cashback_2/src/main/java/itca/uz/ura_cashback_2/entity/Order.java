package itca.uz.ura_cashback_2.entity;

import itca.uz.ura_cashback_2.entity.template.AbsEntity;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "orders")
@Builder
public class Order extends AbsEntity {

    private int companyClientCash;

    private Long companyId;

    @ManyToOne
    private User client;

    private int clientCompCash;

    private int cash_price;

}
