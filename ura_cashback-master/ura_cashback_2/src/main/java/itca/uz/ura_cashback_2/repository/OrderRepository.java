package itca.uz.ura_cashback_2.repository;

import itca.uz.ura_cashback_2.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {
    @Query("select ord from orders ord where ord.createdBy=: createdBy")
    List<Order> findCreatedBy(Long createdBy);

    @Query(value = "select * from orders o where o.company_id= companyId and o.created_at between startTime and endTime", nativeQuery = true)
    List<Optional<Order>> getOrder(Long companyId, Timestamp startTime, Timestamp endTime);

//    @Query("select o from orders o where o.companyId=: companyId and o.createdAt between :startTime and :endTime")
//    List<Optional<Order>> order(Long companyId, Timestamp startTime, Timestamp endTime);

}
