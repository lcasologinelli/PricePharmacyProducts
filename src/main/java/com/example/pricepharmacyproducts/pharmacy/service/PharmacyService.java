package com.example.pricepharmacyproducts.pharmacy.service;


import com.example.pricepharmacyproducts.pharmacy.dto.PharmacyDto;
import com.example.pricepharmacyproducts.pharmacy.entity.Pharmacy;
import com.example.pricepharmacyproducts.pharmacy.mapper.PharmacyMapper;
import com.example.pricepharmacyproducts.pharmacy.repository.PharmacyRepository;
import com.example.pricepharmacyproducts.product.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PharmacyService {
    PharmacyRepository pharmacyRepository;
    ProductRepository productRepository;
    PharmacyMapper pharmacyMapper;

    public PharmacyService(PharmacyRepository pharmacyRepository, ProductRepository productRepository, PharmacyMapper pharmacyMapper) {
        this.pharmacyRepository = pharmacyRepository;
        this.productRepository = productRepository;
        this.pharmacyMapper = pharmacyMapper;
    }

    public Pharmacy savePharmacy(PharmacyDto pharmacyDto){
        if(pharmacyDto.productID().size() != pharmacyDto.price().size()){
            throw new IllegalArgumentException("The list containing the product id" +
                    " and the one containing the prices must have the same length");
        }

        Pharmacy newPharmacy = pharmacyMapper.PharmacyDtoToPharmacy(pharmacyDto);
        return pharmacyRepository.save(newPharmacy);
    }

    public List<Pharmacy> findAllPharmacies(){
        return pharmacyRepository.findAll();
    }

    public Pharmacy findPharmacyById(Integer id){
        return pharmacyRepository.findById(id).orElse(new Pharmacy());
    }

    public List<Pharmacy> findPharmacyByName(String name){
        return pharmacyRepository.findAllByNameContaining(name);
    }

    public void deletePharmacyById(Integer id){
        pharmacyRepository.deleteById(id);
    }

}
