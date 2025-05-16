package com.delivery.spring.Repositories;

import com.delivery.spring.Models.Product;
import org.springframework.data.jpa.repository.JpaRepository;
public interface ProductRepository extends JpaRepository<Product, Long> { }