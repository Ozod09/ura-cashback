package itca.uz.ura_cashback_2.mappers;

import itca.uz.ura_cashback_2.entity.Company;
import itca.uz.ura_cashback_2.payload.CompanyDto;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;


@Mapper(componentModel = "spring")
public interface CompanyMapper {

    Company fromDto(CompanyDto companyDto);

    CompanyDto fromCompany(Company company);


}
