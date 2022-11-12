package itca.uz.ura_cashback_2.mappers;

import itca.uz.ura_cashback_2.entity.User;
import itca.uz.ura_cashback_2.payload.AuthDto;
import org.mapstruct.Mapper;


@Mapper(componentModel = "spring")
public interface UserMapper {

    User fromDto(AuthDto authDto);

    AuthDto fromUser(User user);
}
