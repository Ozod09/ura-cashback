package itca.uz.ura_cashback_2.repository;

import itca.uz.ura_cashback_2.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


public interface
AuthRepository extends JpaRepository<User, Long> {
    @Query("select u from users u where u.phoneNumber=: phoneNumber and u.email=: email")
    User equalsUser(String phoneNumber, String email);

    User findByIdEquals(Long id);

    @Query("select u from users u where u.phoneNumber=: phoneNumber and u.password=: password")
    User findPhoneAndPassword(String phoneNumber, String password);

    @Query("select u from users u where u.phoneNumber=: phoneNumber")
    User findUserPhone(String phoneNumber);
}
