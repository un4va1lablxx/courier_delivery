package com.delivery.spring.Repositories;

import com.delivery.spring.Models.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Customer findByLogin(String login);
    @Query("SELECT c FROM Customer c WHERE c.login = :login")
    Customer findFirstByLogin(@Param("login") String login);
}