package com.example.pricepharmacyproducts.pharmacy;

import com.example.pricepharmacyproducts.pharmacy.Pharmacy;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PharmacyRepository extends JpaRepository<Pharmacy,Integer> {

    List<Pharmacy> findAllByNameContaining(String p);

}
