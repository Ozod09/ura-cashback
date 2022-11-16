package itca.uz.ura_cashback_2.repository;

import itca.uz.ura_cashback_2.entity.CompanyUserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CompanyUserRoleRepository extends JpaRepository<CompanyUserRole, Long> {

//    @Query("select cur from CompanyUserRole cur where cur.userId=: userId and cur.roleId=:roleId")
//    Optional<CompanyUserRole> findByUserAndRole(Long userId, Integer roleId);

    @Query("select cur from CompanyUserRole cur where cur.userId=: userId")
    Optional<CompanyUserRole> findId(Long userId);

//    @Query(value = "select cur.userId from CompanyUserRole cur where cur.companyId=:companyId and cur.roleId=: roleId")
    List<CompanyUserRole> findByCompanyIdEqualsAndRoleIdEquals(Long companyId, Integer roleId);

    @Query("select cur.userId from CompanyUserRole cur where cur.companyId =: companyId and cur.roleId=: adminRole or cur.roleId =: superAdminRole or cur.roleId =: kasserRole")
    List<Long> getCompanyRole(Long companyId, Integer adminRole, Integer superAdminRole, Integer kasserRole);

    @Query("select c from CompanyUserRole  c where c.userId=:userId and c.roleId=:roleId")
    Optional<CompanyUserRole> deleteKassir(Long userId, Integer roleId);
}
