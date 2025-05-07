package com.example.TTTN.repository;

import com.example.TTTN.entity.Order;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OrderRepository extends JpaRepository<Order, Long> {
        Page<Order> findByPartnerId(long partnerId, Pageable pageable);

        @Query(value = "SELECT " +
                        "YEAR(STR_TO_DATE(o.created_at, '%Y-%m-%d')) AS year, " +
                        "MONTH(STR_TO_DATE(o.created_at, '%Y-%m-%d')) AS month, " +
                        "SUM(o.total_money) AS revenue, " +
                        "SUM(o.profit_money) AS profit " +
                        "FROM orders o " +
                        "WHERE o.order_status_id IN (2, 3) " +
                        "AND YEAR(STR_TO_DATE(o.created_at, '%Y-%m-%d')) = :year " +
                        "GROUP BY " +
                        "YEAR(STR_TO_DATE(o.created_at, '%Y-%m-%d')), " +
                        "MONTH(STR_TO_DATE(o.created_at, '%Y-%m-%d')) " +
                        "ORDER BY " +
                        "YEAR(STR_TO_DATE(o.created_at, '%Y-%m-%d')) DESC, " +
                        "MONTH(STR_TO_DATE(o.created_at, '%Y-%m-%d')) DESC", nativeQuery = true)
        List<Object[]> findRevenueByMonth(@Param("year") int year);

        @Query(value = "SELECT YEAR(STR_TO_DATE(o.created_at, '%Y-%m-%d')) as year, " +
                        "QUARTER(STR_TO_DATE(o.created_at, '%Y-%m-%d')) as quarter, " +
                        "SUM(o.total_money) as revenue, " +
                        "SUM(o.profit_money) as profit " +
                        "FROM orders o " +
                        "WHERE o.order_status_id IN (2, 3) " +
                        "AND YEAR(STR_TO_DATE(o.created_at, '%Y-%m-%d')) = :year " +
                        "GROUP BY YEAR(STR_TO_DATE(o.created_at, '%Y-%m-%d')), " +
                        "QUARTER(STR_TO_DATE(o.created_at, '%Y-%m-%d')) " +
                        "ORDER BY YEAR(STR_TO_DATE(o.created_at, '%Y-%m-%d')) DESC, " +
                        "QUARTER(STR_TO_DATE(o.created_at, '%Y-%m-%d')) DESC", nativeQuery = true)
        List<Object[]> findRevenueByQuarter(@Param("year") int year);

        @Query(value = "SELECT YEAR(STR_TO_DATE(o.created_at, '%Y-%m-%d')) as year, " +
                        "SUM(o.total_money) as revenue, " +
                        "SUM(o.profit_money) as profit " +
                        "FROM orders o " +
                        "WHERE o.order_status_id IN (2, 3) " +
                        "GROUP BY YEAR(STR_TO_DATE(o.created_at, '%Y-%m-%d')) " +
                        "ORDER BY YEAR(STR_TO_DATE(o.created_at, '%Y-%m-%d')) DESC", nativeQuery = true)
        List<Object[]> findRevenueByYear();

}
