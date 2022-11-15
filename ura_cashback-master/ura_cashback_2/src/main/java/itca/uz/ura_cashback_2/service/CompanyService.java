package itca.uz.ura_cashback_2.service;

import itca.uz.ura_cashback_2.entity.Company;
import itca.uz.ura_cashback_2.entity.CompanyUserRole;
import itca.uz.ura_cashback_2.entity.enums.RoleName;
import itca.uz.ura_cashback_2.exception.ResourceNotFoundException;
import itca.uz.ura_cashback_2.mappers.CompanyMapper;
import itca.uz.ura_cashback_2.payload.ApiResponse;
import itca.uz.ura_cashback_2.payload.CompanyDto;
import itca.uz.ura_cashback_2.repository.AttachmentRepository;
import itca.uz.ura_cashback_2.repository.CompanyRepository;
import itca.uz.ura_cashback_2.repository.CompanyUserRoleRepository;
import itca.uz.ura_cashback_2.repository.RoleRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CompanyService {

    private final CompanyRepository companyRepository;
    private final AttachmentRepository attachmentRepository;
    private final CompanyUserRoleRepository companyUserRoleRepository;
    private final RoleRepository roleRepository;
    private final CompanyMapper companyMapper;

    public CompanyService(CompanyRepository companyRepository, AttachmentRepository attachmentRepository, CompanyUserRoleRepository companyUserRoleRepository, RoleRepository roleRepository, CompanyMapper companyMapper) {
        this.companyRepository = companyRepository;
        this.attachmentRepository = attachmentRepository;
        this.companyUserRoleRepository = companyUserRoleRepository;
        this.roleRepository = roleRepository;
        this.companyMapper = companyMapper;
    }

    public ApiResponse<?> addCompany(CompanyDto companyDto) {
        return addOrEditCompany(companyDto);
    }

    public ApiResponse<?> editCompany(CompanyDto companyDto, Company company) {
       return addOrEditCompany(companyDto);
    }

    public List<CompanyDto> getCompanyList(){
        List<Company> all = companyRepository.findAll();
        List<CompanyDto> companyDtoList = new ArrayList<>();
        for (Company company : all) {
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
            if (company.getActive()==1){
                company.setActive((byte) 0);
                companyRepository.save(company);
                return new ApiResponse<>("Company inactive", 200);
            }else{
                company.setActive((byte) 1);
                companyRepository.save(company);
                return new ApiResponse<>("Company active", 200);
            }
        }
        return new ApiResponse<>("Company not found", 401);
    }

    public ApiResponse<?> addOrEditCompany(CompanyDto companyDto){
        Company company = companyMapper.fromDto(companyDto);
        company.setActive((byte) (companyDto.isActive1() ? 1 : 0));
        company.setAttachment(attachmentRepository.findById(companyDto.getAttachmentId())
                .orElseThrow(() -> new ResourceAccessException("GetAttachment")));
        Company saveCompany = companyRepository.save(company);
        //companyUser
        CompanyUserRole companyUserRole = new CompanyUserRole();
        companyUserRole.setCompanyId(saveCompany.getId());
        companyUserRole.setRoleId(2);
        companyUserRole.setUserId(companyDto.getUserId());
        companyUserRoleRepository.save(companyUserRole);
        return new ApiResponse<>("Successfully saved company", 200);
    }
}
