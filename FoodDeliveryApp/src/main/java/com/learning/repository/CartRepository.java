package com.learning.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.learning.dto.Cart;

public interface CartRepository extends JpaRepository<Cart, Long> {

}
