package com.duy.do_an_be.repository;
import com.duy.do_an_be.entity.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderDetailRepository extends JpaRepository<OrderDetail, Long> {
    @Query("SELECT SUM(od.quantity) FROM OrderDetail od WHERE od.option.product.proId = :proId")
    Long sumQuantityByProductId(@Param("proId") Long proId);

    @Query("SELECT SUM(od.quantity) FROM OrderDetail od WHERE od.option.optionId = :optionId")
    Long sumQuantityByOptionId(@Param("optionId") Long optionId);

    List<OrderDetail> findByOrderOrderId(Long orderId);
}
