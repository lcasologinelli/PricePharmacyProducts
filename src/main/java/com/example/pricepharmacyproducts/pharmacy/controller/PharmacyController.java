package com.example.pricepharmacyproducts.pharmacy.controller;

import com.example.pricepharmacyproducts.pharmacy.dto.PharmacyDto;
import com.example.pricepharmacyproducts.pharmacy.dto.PharmacyResponseDto;
import com.example.pricepharmacyproducts.pharmacy.entity.Pharmacy;
import com.example.pricepharmacyproducts.pharmacy.mapper.PharmacyMapper;
import com.example.pricepharmacyproducts.pharmacy.repository.PharmacyRepository;
import com.example.pricepharmacyproducts.pharmacy.service.PharmacyService;
import com.example.pricepharmacyproducts.product.entity.Product;
import com.example.pricepharmacyproducts.product.repository.ProductRepository;
import com.example.pricepharmacyproducts.sale.entity.Sale;
import com.example.pricepharmacyproducts.sale.key.SaleKey;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@RestController
@RequestMapping("pharmacies")
public class PharmacyController {

    private final PharmacyService pharmacyService;
    private final PharmacyMapper pharmacyMapper;

    public PharmacyController(PharmacyService pharmacyService, PharmacyMapper pharmacyMapper) {
        this.pharmacyService = pharmacyService;
        this.pharmacyMapper = pharmacyMapper;
    }

    @PostMapping
    public PharmacyResponseDto savePharmacy(
            @Valid @RequestBody PharmacyDto pharmacyDto
    ){
        return pharmacyMapper.toPharmacyResponseDto(this.pharmacyService.savePharmacy(pharmacyDto));
    }

    @GetMapping
    public List<PharmacyResponseDto> findAllPharmacies(){
        return this.pharmacyService.findAllPharmacies()
                .stream().
                map(pharmacyMapper::toPharmacyResponseDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{pharmacy_id}")
    public PharmacyResponseDto findPharmacyById(
            @PathVariable("pharmacy_id") Integer id
    ){
        return pharmacyMapper.toPharmacyResponseDto(this.pharmacyService.findPharmacyById(id));
    }

    @GetMapping("/search/{p_name}")
    public List<PharmacyResponseDto> findPharmacyByName(
            @PathVariable("p_name") String name
    ){
        return this.pharmacyService.findPharmacyByName(name)
                .stream().
                map(pharmacyMapper::toPharmacyResponseDto)
                .collect(Collectors.toList());
    }

    @DeleteMapping("/{pharmacy_delete}")
    @ResponseStatus(HttpStatus.OK)
    public void delete(
            @PathVariable("pharmacy_delete") Integer id
    ){
        this.pharmacyService.deletePharmacyById(id);
    }

}
