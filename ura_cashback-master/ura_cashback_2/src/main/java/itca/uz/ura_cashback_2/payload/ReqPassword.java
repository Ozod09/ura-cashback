package itca.uz.ura_cashback_2.payload;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReqPassword {
    private Long userId;

    private String joriyPassword;

    private String password;

    private String prePassword;

}
