package com.example.pricepharmacyproducts.pharmacy;


import com.example.pricepharmacyproducts.product.Product;
import com.example.pricepharmacyproducts.product.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;


@Controller
@RequestMapping("pharmacies")
public class PharmacyController {

    private final PharmacyService pharmacyService;
    private final PharmacyMapper pharmacyMapper;
    private final ProductService productService;

    public PharmacyController(PharmacyService pharmacyService, PharmacyMapper pharmacyMapper, ProductService productService) {
        this.pharmacyService = pharmacyService;
        this.pharmacyMapper = pharmacyMapper;
        this.productService = productService;
    }

    @GetMapping("/newPharmacy")
    public String createPharmacyForm(Model model){
        model.addAttribute("pharmacyDto",new PharmacyDto());
        return "create_pharmacy";
    }
    @PostMapping()
    public String savePharmacy(
            @ModelAttribute("pharmacyDto") PharmacyDto pharmacyDto
    ){
        this.pharmacyService.savePharmacy(pharmacyDto);
        return"redirect:/pharmacies";

    }

    @GetMapping
    public String findAllPharmacies(Model model, @RequestParam(value = "keyword", required = false) String keyword) {
        List<PharmacyDto> pharmacyList;
        if (keyword != null && !keyword.isEmpty()) {
            pharmacyList = pharmacyService.findPharmacyByName(keyword)
                    .stream()
                    .map(pharmacyMapper::toDto).collect(Collectors.toList());
        } else {
            pharmacyList = pharmacyService.findAllPharmacies();
        }
        model.addAttribute("pharmacy_list", pharmacyList);
        model.addAttribute("keyword", keyword);
        return "pharmacy_list";
    }
    @GetMapping("/edit/{id}")
    public String editPharmacyForm(@PathVariable Integer id, Model model){
        List<Product> products = productService.findAllProducts();
        model.addAttribute("pharmacyDto",pharmacyService.findPharmacyById(id));
        model.addAttribute("products",products);
        return "edit_pharmacy";
    }
    @PostMapping("/{id}")
    public String updatePharmacy(
            @PathVariable Integer id,
            @ModelAttribute("pharmacyDto") PharmacyDto pharmacyDto){

        PharmacyDto findPharmacyDto = pharmacyService.findPharmacyById(id);
        findPharmacyDto.setWebAddress(pharmacyDto.getWebAddress());
        findPharmacyDto.setCity(pharmacyDto.getCity());
        findPharmacyDto.setName(pharmacyDto.getName());
        findPharmacyDto.setSales(pharmacyDto.getSales());
        findPharmacyDto.setShippingFees(pharmacyDto.getShippingFees());
        findPharmacyDto.setFreeShipping(pharmacyDto.getFreeShipping());

        pharmacyService.updatePharmacy(findPharmacyDto);
        return "redirect:/pharmacies";
    }

    @GetMapping("/{pharmacy_id}")
    public ResponseEntity<PharmacyDto> findPharmacyById(
            @PathVariable("pharmacy_id") Integer id
    ){
        return ResponseEntity.ok().body(this.pharmacyService.findPharmacyById(id));
    }

    @GetMapping("/search/{p_name}")
    public ResponseEntity<List<Pharmacy>> findPharmacyByName(
            @PathVariable("p_name") String name
    ){
        return ResponseEntity.ok().body(this.pharmacyService.findPharmacyByName(name));
    }

    @GetMapping("/delete/{id}")
    public String delete(
            @PathVariable("id") Integer id
    ){
        pharmacyService.deletePharmacyById(id);
        return"redirect:/pharmacies";
    }

}
