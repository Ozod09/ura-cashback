package itca.uz.ura_cashback_2.payload;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResStatistic {
    //company dagi client size
    private Integer jamiClient;

    //company dagi opshi summa
    private int allBalance;

    //company ga clientlar qancha naqt tulagani
    private int clientNaqtTulovComp;

    //company ga clientlar qancha cashbackdan tulagani
    private int clientCompCash;

    //company clientlarga qancha cashback qaytargani
    private int companyClientCash;

    //companydagi clientlarning qancha cashbacki borligi
    private int clientCash;
}
