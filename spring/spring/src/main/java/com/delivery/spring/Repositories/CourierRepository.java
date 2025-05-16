package com.delivery.spring.Repositories;

import com.delivery.spring.Models.Courier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CourierRepository extends JpaRepository<Courier, Long> {
    Courier findByLogin(String login);
    @Query(value = "SELECT * FROM couriers WHERE status = :status ORDER BY RANDOM() LIMIT 1", nativeQuery = true)
    Courier findRandomCourierByStatus(@Param("status") String status);
}