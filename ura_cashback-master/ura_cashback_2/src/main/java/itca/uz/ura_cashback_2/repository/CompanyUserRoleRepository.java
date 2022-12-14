package itca.uz.ura_cashback_2.repository;

import itca.uz.ura_cashback_2.entity.CompanyUserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CompanyUserRoleRepository extends JpaRepository<CompanyUserRole, Long> {


    @Query("select cur from CompanyUserRole cur where cur.userId = ?1")
    Optional<CompanyUserRole> findId(Long userId);

    @Query(value = "select cur from CompanyUserRole cur where cur.companyId = ?1 and cur.roleId = ?2")
    List<CompanyUserRole> companyIdAndRoleId(Long companyId, Integer roleId);


    @Query("select c from CompanyUserRole  c where c.userId = ?1 and c.roleId = ?2")
    Optional<CompanyUserRole> getKassir(Long userId, int roleId);

    @Query("select kassir from CompanyUserRole kassir where kassir.userId = ?1 and kassir.roleId = ?2")
    Optional<CompanyUserRole> kassir(Long userId, Integer roleId);

    Optional<List<CompanyUserRole>> findByCompanyIdEquals(Long companyId);

    Optional<List<CompanyUserRole>> findByCompanyIdEqualsAndRoleIdEquals(Long companyId, Integer roleId);
}
