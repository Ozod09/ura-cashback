package itca.uz.ura_cashback_2.payload;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReqStatistic {
    private Long companyId;

    private String startDate;

    private String finishDate;
}
