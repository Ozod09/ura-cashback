package itca.uz.ura_cashback_2.payload;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ResStatistic {
    //clientlar soni
    private Integer jamiClient;

    //company dagi opshi summa
    private int allBalance;

    //tulangan cash
    private int companyClientCash;

    //mijozlar salary
    private int clientCash;

    //o'rtacha check
    private int urtachaCheck;
}

