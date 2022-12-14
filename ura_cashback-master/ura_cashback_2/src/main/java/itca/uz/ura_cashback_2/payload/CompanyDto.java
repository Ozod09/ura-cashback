package itca.uz.ura_cashback_2.payload;

import itca.uz.ura_cashback_2.entity.Attachment;
import itca.uz.ura_cashback_2.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CompanyDto {

    private Long id;

    private String name;

    private String bio;

    private String description;

    private int clientPercentage;

    private Attachment attachment;
    private Long attachmentId;

    private Long userId;
    private User user;

    private ResStatistic resStatistic;

    private boolean active1;
}