package itca.uz.ura_cashback_2.payload;

import itca.uz.ura_cashback_2.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Pattern;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AuthDto {

    private Long id;

    private String firstName;

    private String lastName;

    @Pattern(regexp = "^+?([9]{2}[8])?[0-9]{9}$",message = "Phone number error",groups = Error.class)
    private String phoneNumber;

    private String email;

    private int salary;

    private byte active;

    private String password;

    private String prePassword;

    private Long companyId;

    private List<Role> roles;
}
