package com.example.pricepharmacyproducts.pharmacy.repository;

import com.example.pricepharmacyproducts.pharmacy.entity.Pharmacy;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PharmacyRepository extends JpaRepository<Pharmacy,Integer> {

    List<Pharmacy> findAllByNameContaining(String p);

}
