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
        if (companyRepository.existsName(companyDto.getName())!= null) {
            if (companyRepository.existsAttachment(companyDto.getAttachmentId())!= null) {
                Company company = companyMapper.fromDto(companyDto);
                company.setAttachment(attachmentRepository.findById(companyDto.getAttachmentId())
                        .orElseThrow(() -> new ResourceAccessException("GetAttachment")));
                Company saveCompany = companyRepository.save(company);
                //companyUser
                CompanyUserRole companyUserRole = new CompanyUserRole();
                companyUserRole.setCompanyId(saveCompany.getId());
                companyUserRole.setRoleId(roleRepository.findRoleName(RoleName.ROLE_ADMIN).orElseThrow(() -> new ResourceNotFoundException(404, "Role", "roleName", companyDto.getUserId())).getId());
                companyUserRole.setUserId(companyDto.getUserId());
                companyUserRoleRepository.save(companyUserRole);
                return new ApiResponse<>("Successfully saved company", 200);
            }
            return new ApiResponse<>("Attachment already exist", 401);
        }
        return new ApiResponse<>("Name already exist", 401);
    }
}
