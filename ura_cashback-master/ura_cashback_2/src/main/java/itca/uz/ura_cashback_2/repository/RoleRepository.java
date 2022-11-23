package itca.uz.ura_cashback_2.repository;

import itca.uz.ura_cashback_2.entity.Role;
import itca.uz.ura_cashback_2.entity.enums.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.Optional;

@CrossOrigin
public interface RoleRepository extends JpaRepository<Role, Integer> {

    @Query("select role from roles role where role.roleName = ?1")
    Optional<Role> findRoleName(RoleName roleUser);


    @Query("select role from roles role where role.id = ?1")
    Optional<Role> findId(Integer id);


}
