package itca.uz.ura_cashback_2.repository;

import itca.uz.ura_cashback_2.entity.Role;
import itca.uz.ura_cashback_2.entity.enums.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.Optional;

@CrossOrigin
public interface RoleRepository extends JpaRepository<Role, Integer> {

    @Query(value = "select role from roles role where role.roleName =: roleUser")
    Optional<Role> findRoleName(RoleName roleUser);

    @Query("select role from roles role where role.id=: id")
    Optional<Role> findId(Integer id);

//    @Query(value = "select * from roles   where role_name =:roleName",nativeQuery = true)
//    Role findRoleBy(@Param("roleName") RoleName roleName);
    Role findRoleByRoleNameEquals(RoleName roleName);
}
