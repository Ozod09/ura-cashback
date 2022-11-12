package itca.uz.ura_cashback_2.repository;

import itca.uz.ura_cashback_2.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CompanyRepository extends JpaRepository<Company, Long> {


    @Query( "select com from Company  com where com.name=: name")
    Company existsName(String name);

    @Query("select com from Company com where com.attachment.id=: attachment_id")
    Company existsAttachment(Long attachment_id);

    @Query("select c from Company  c where  c.active =:true and c.id=:id")
    Company comActive(Long id);

}
