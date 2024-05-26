package com.example.pricepharmacyproducts.pharmacy;


import com.example.pricepharmacyproducts.exception.IdNotFoundException;
import com.example.pricepharmacyproducts.product.ProductRepository;
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
            throw new RuntimeException("The list containing the product id" +
                    " and the one containing the prices must have the same length");
        }

        Pharmacy newPharmacy = pharmacyMapper.PharmacyDtoToPharmacy(pharmacyDto);
        return pharmacyRepository.save(newPharmacy);
    }

    public List<Pharmacy> findAllPharmacies(){
        return pharmacyRepository.findAll();
    }

    public Pharmacy findPharmacyById(Integer id){
        return pharmacyRepository.findById(id).orElseThrow(()-> new IdNotFoundException("No Pharmacy by ID: "+ id));
    }

    public List<Pharmacy> findPharmacyByName(String name){
        return pharmacyRepository.findAllByNameContaining(name);
    }

    public void deletePharmacyById(Integer id){
        pharmacyRepository.deleteById(id);
    }

}
