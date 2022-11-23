package itca.uz.ura_cashback_2.repository;

import itca.uz.ura_cashback_2.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;


public interface
AuthRepository extends JpaRepository<User, Long> {

    @Query("select u from users u where u.phoneNumber = ?1 and u.email = ?2")
    Optional<User> equalsUser(String phoneNumber, String email);

    @Query("select u from users u where u.phoneNumber = ?1 and u.password = ?2")
    Optional<User> findPhoneAndPassword(String phoneNumber, String password);

    @Query("select u from users u where u.phoneNumber = ?1")
    Optional<User> findByPhoneNumber(String phoneNumber);
}
