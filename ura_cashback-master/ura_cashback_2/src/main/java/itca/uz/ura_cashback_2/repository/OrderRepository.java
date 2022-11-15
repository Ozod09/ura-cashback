package itca.uz.ura_cashback_2.repository;

import itca.uz.ura_cashback_2.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.sql.Timestamp;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    @Query("select ord from orders ord where ord.createdBy=: createdBy")
    List<Order> findCreatedBy(Long createdBy);

    @Query(value = "select * from orders o where o.companyId= companyId and o.createdAt between startTime and endTime", nativeQuery = true)
    List<Order> getOrder(Long companyId, Timestamp startTime, Timestamp endTime);

}
