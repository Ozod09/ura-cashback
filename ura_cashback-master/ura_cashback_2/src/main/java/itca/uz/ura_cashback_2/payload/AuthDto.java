package itca.uz.ura_cashback_2.payload;

import itca.uz.ura_cashback_2.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AuthDto {

    private Long id;

    private String firstName;

    private String lastName;

    private String phoneNumber;

    private String email;

    private int salary;

    private byte active;

    private String password;

    private String prePassword;

    private Long companyId;

    private List<Role> roles;



}
