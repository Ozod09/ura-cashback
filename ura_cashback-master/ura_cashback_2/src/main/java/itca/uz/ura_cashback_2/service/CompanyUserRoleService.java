package itca.uz.ura_cashback_2.service;

import itca.uz.ura_cashback_2.entity.Company;
import itca.uz.ura_cashback_2.entity.CompanyUserRole;
import itca.uz.ura_cashback_2.repository.CompanyUserRoleRepository;
import org.springframework.stereotype.Service;


@Service
public class CompanyUserRoleService {

    final CompanyUserRoleRepository companyUserRoleRepository;
    final CompanyService companyService;

    public CompanyUserRoleService(CompanyUserRoleRepository companyUserRoleRepository, CompanyService companyService) {
        this.companyUserRoleRepository = companyUserRoleRepository;
        this.companyService = companyService;
    }

    public void addCompanyUserRole(CompanyUserRole companyUserRole, Long userId, Long companyId, Integer roleId) {
        companyUserRole = CompanyUserRole.builder()
                .companyId(companyId)
                .roleId(roleId)
                .userId(userId).build();
        companyUserRoleRepository.save(companyUserRole);
    }

    public Company getCompanyFindByUser(Long userId, Integer roleId) {
        return companyService.getOneCompany(companyUserRoleRepository.findByUserAndRole(userId, roleId).getCompanyId());
    }
}