package com.example.pricepharmacyproducts.user;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<Order,Integer> {
}
