package com.example.pricepharmacyproducts.pharmacy;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
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
    public ResponseEntity<PharmacyResponseDto> savePharmacy(
            @Valid @RequestBody PharmacyDto pharmacyDto
    ){
        return ResponseEntity.ok().body(pharmacyMapper.toPharmacyResponseDto(this.pharmacyService.savePharmacy(pharmacyDto)));
    }

    @GetMapping
    public ResponseEntity<List<PharmacyResponseDto>> findAllPharmacies(){
        return ResponseEntity.ok().body(this.pharmacyService.findAllPharmacies()
                .stream().
                map(pharmacyMapper::toPharmacyResponseDto)
                .collect(Collectors.toList()));
    }

    @GetMapping("/{pharmacy_id}")
    public ResponseEntity<PharmacyResponseDto> findPharmacyById(
            @PathVariable("pharmacy_id") Integer id
    ){
        return ResponseEntity.ok().body(pharmacyMapper.toPharmacyResponseDto(this.pharmacyService.findPharmacyById(id)));
    }

    @GetMapping("/search/{p_name}")
    public ResponseEntity<List<PharmacyResponseDto>> findPharmacyByName(
            @PathVariable("p_name") String name
    ){
        return ResponseEntity.ok().body(this.pharmacyService.findPharmacyByName(name)
                .stream().
                map(pharmacyMapper::toPharmacyResponseDto)
                .collect(Collectors.toList()));
    }

    @DeleteMapping("/{pharmacy_delete}")
    @ResponseStatus(HttpStatus.OK)
    public void delete(
            @PathVariable("pharmacy_delete") Integer id
    ){
        this.pharmacyService.deletePharmacyById(id);
    }

}
