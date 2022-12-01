package itca.uz.ura_cashback_2.service;

import itca.uz.ura_cashback_2.entity.Company;
import itca.uz.ura_cashback_2.mappers.CompanyMapper;
import itca.uz.ura_cashback_2.payload.ApiResponse;
import itca.uz.ura_cashback_2.payload.CompanyDto;
import itca.uz.ura_cashback_2.repository.AttachmentRepository;
import itca.uz.ura_cashback_2.repository.CompanyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CompanyService {

    private final CompanyRepository companyRepository;
    private final AttachmentRepository attachmentRepository;
    private final CompanyMapper companyMapper;
    private final @Lazy CompanyUserRoleService companyUserRoleService;

    public ApiResponse<?> addCompany(CompanyDto companyDto) {
        Company company = companyMapper.fromDto(companyDto);
        company.setActive((byte) (companyDto.isActive1() ? 1 : 0));
        company.setAttachment(attachmentRepository.findById(companyDto.getAttachmentId())
                .orElseThrow(() -> new ResourceAccessException("GetAttachment")));
        final Company save = companyRepository.save(company);
        companyUserRoleService.addCompanyUserRole(companyDto.getUserId(), save.getId(), 2);
        return new ApiResponse<>("Successfully saved company", 200);
    }

    public ApiResponse<?> editCompany(CompanyDto companyDto, Company company) {
        Company company1 = companyMapper.fromDto(companyDto);
        company1.setId(company.getId());
        company1.setActive(company.getActive());
        company1.setAttachment(attachmentRepository.findById(companyDto.getAttachmentId())
                .orElseThrow(() -> new ResourceAccessException("GetAttachment")));
        companyRepository.save(company1);
        return new ApiResponse<>("Successfully saved company", 200);
    }

    public List<CompanyDto> getCompanyList() {
        List<CompanyDto> companyDtoList = new ArrayList<>();
        for (Company company : companyRepository.findAll()) {
            CompanyDto companyDto = companyMapper.fromCompany(company);
            companyDto.setActive1(company.getActive() == 1);
            companyDtoList.add(companyDto);
        }
        return companyDtoList;
    }

    public Company getOneCompany(Long companyId) {
        return companyRepository.findById(companyId).orElseThrow(() -> new ResourceAccessException("GetCompany"));
    }

    public ApiResponse<?> changeActiveCom(Long id) {
        Optional<Company> byId = companyRepository.findById(id);
        if (byId.isPresent()) {
            Company company = byId.orElseThrow(() -> new ResourceAccessException("GetCompany"));
            if (company.getActive() == 1) {
                company.setActive((byte) 0);
                companyRepository.save(company);
                return new ApiResponse<>("Company inactive", 200);
            } else {
                company.setActive((byte) 1);
                companyRepository.save(company);
                return new ApiResponse<>("Company active", 200);
            }
        }
        return new ApiResponse<>("Company not found", 401);
    }
}
