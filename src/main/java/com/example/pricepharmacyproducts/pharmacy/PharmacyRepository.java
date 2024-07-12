package com.example.pricepharmacyproducts.pharmacy;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PharmacyRepository extends JpaRepository<Pharmacy,Integer> {

    List<Pharmacy> findAllByNameContainingIgnoreCase(String p);

}
