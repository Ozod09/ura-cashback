package itca.uz.ura_cashback_2.repository;

import itca.uz.ura_cashback_2.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;


public interface
AuthRepository extends JpaRepository<User, Long> {
    @Query("select u from users u where u.phoneNumber=: phoneNumber and u.email=: email")
    Optional<User> equalsUser(String phoneNumber, String email);

    @Query("select u from users u where u.phoneNumber=: phoneNumber and u.password=: password")
    Optional<User> findPhoneAndPassword(String phoneNumber, String password);

//    @Query("select u from users u where u.phoneNumber=:phoneNumber")
    Optional<User> findByPhoneNumber(String phoneNumber);
}
