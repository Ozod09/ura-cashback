package itca.uz.ura_cashback_2.repository;

import itca.uz.ura_cashback_2.entity.Company;
import itca.uz.ura_cashback_2.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface CompanyRepository extends JpaRepository<Company, Long> {


    @Query( "select com from Company  com where com.name = ?1")
    Optional<Company> existsName(String name);

    @Query("select com from Company com where com.attachment.id = ?1")
    Company existsAttachment(Long attachment_id);

    @Query("select c from Company  c where  c.active = 1 and c.id = ?1")
    Company comActive(Long id);

}
