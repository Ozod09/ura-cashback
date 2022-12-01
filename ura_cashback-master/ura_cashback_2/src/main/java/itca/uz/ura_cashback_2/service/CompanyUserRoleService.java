package itca.uz.ura_cashback_2.service;

import itca.uz.ura_cashback_2.entity.Company;
import itca.uz.ura_cashback_2.entity.CompanyUserRole;
import itca.uz.ura_cashback_2.entity.User;
import itca.uz.ura_cashback_2.exception.ResourceNotFoundException;
import itca.uz.ura_cashback_2.repository.CompanyUserRoleRepository;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;

import java.util.ArrayList;
import java.util.List;


@Service
public class CompanyUserRoleService {

    private final CompanyUserRoleRepository companyUserRoleRepository;
    private final CompanyService companyService;
    final AuthService authService;

    public CompanyUserRoleService(CompanyUserRoleRepository companyUserRoleRepository, @Lazy CompanyService companyService, @Lazy AuthService authService) {
        this.companyUserRoleRepository = companyUserRoleRepository;
        this.companyService = companyService;
        this.authService = authService;
    }

    public void addCompanyUserRole(Long userId, Long companyId, Integer roleId) {
        CompanyUserRole companyUserRole = CompanyUserRole.builder()
                .companyId(companyId)
                .roleId(roleId)
                .userId(userId).build();
        companyUserRoleRepository.save(companyUserRole);
    }

    public List<User> getFainByCompany(Long id) {
        List<User> userList = new ArrayList<>();
        for (CompanyUserRole getCompanyUserRole : companyUserRoleRepository.findByCompanyIdEquals(id).
                orElseThrow(() -> new ResourceAccessException("getCompanyUserRole"))) {
            userList.add(authService.getOneUser(getCompanyUserRole.getUserId()));
        }
        return userList;
    }

    public Company getCompanyFindByUser(Long userId, Integer roleId) {
        return companyService.getOneCompany(companyUserRoleRepository.getKassir(userId, roleId).orElseThrow(() -> new ResourceNotFoundException(404, "companyUserRole", "id", userId)).getCompanyId());
    }

    public List<User> getUserFindByCompanyAndRole(Long companyId, Integer roleId) {
        List<User> userList = new ArrayList<>();
        for (CompanyUserRole getUser : companyUserRoleRepository.findByCompanyIdEqualsAndRoleIdEquals(companyId, roleId)
                .orElseThrow(() -> new ResourceAccessException("getCompanyUserRole"))) {
            userList.add(authService.getOneUser(getUser.getUserId()));
        }
        return userList;
    }


}
