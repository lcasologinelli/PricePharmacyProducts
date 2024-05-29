package com.example.pricepharmacyproducts.pharmacy;


import com.example.pricepharmacyproducts.exception.IdNotFoundException;
import com.example.pricepharmacyproducts.product.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

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

        Pharmacy newPharmacy = pharmacyMapper.toPharmacy(pharmacyDto);
        return pharmacyRepository.save(newPharmacy);
    }

    public List<PharmacyDto> findAllPharmacies(){
        return pharmacyRepository.findAll()
                .stream()
                .map(pharmacy -> pharmacyMapper.toDto(pharmacy)).collect(Collectors.toList());
    }

    public PharmacyDto findPharmacyById(Integer id){
        return pharmacyMapper.toDto(pharmacyRepository.findById(id).orElseThrow(()-> new IdNotFoundException("No Pharmacy by ID: "+ id)));
    }

    public List<Pharmacy> findPharmacyByName(String name){
        return pharmacyRepository.findAllByNameContaining(name);
    }

    public void deletePharmacyById(Integer id){
        pharmacyRepository.deleteById(id);
    }

    public void updatePharmacy(PharmacyDto pharmacyDto) {
        pharmacyRepository.save(pharmacyMapper.toPharmacy(pharmacyDto));
    }
}
