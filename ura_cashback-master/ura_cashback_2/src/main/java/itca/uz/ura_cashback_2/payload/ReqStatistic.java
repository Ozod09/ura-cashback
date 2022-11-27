package itca.uz.ura_cashback_2.payload;

import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
public class ReqStatistic {

    private Long companyId;

    private LocalDateTime startTime;

    private LocalDateTime finishTime;

}
