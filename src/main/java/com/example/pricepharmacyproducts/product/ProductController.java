package com.example.pricepharmacyproducts.product;

import jakarta.persistence.PostUpdate;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@Controller
@RequestMapping("/products")
public class ProductController {

    ProductService productService;


    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/newProduct")
    public String createProductForm(Model model){
        Product product = new Product();
        model.addAttribute("product",product);
        return "create_product";
    }

    @PostMapping
    public String saveProduct(
            @ModelAttribute("product") Product product
    ){
        this.productService.saveProduct(product);
        return "redirect:/products";
    }

    @GetMapping("/edit/{id}")
    public String editProductForm(@PathVariable Integer id, Model model){
        model.addAttribute("product",productService.findProductById(id));
        return "edit_product";
    }

    @PostMapping("/{id}")
    public String updateProduct(
            @PathVariable Integer id,
            @ModelAttribute("product") Product product,
            Model model){

        Product existingProduct = productService.findProductById(id);
        existingProduct.setProduct_id(id);
        existingProduct.setCategory(product.getCategory());
        existingProduct.setDose(product.getDose());
        existingProduct.setName(product.getName());
        existingProduct.setFormat(product.getFormat());
        existingProduct.setProducer(product.getProducer());

        productService.updateProduct(existingProduct);
        return "redirect:/products";
    }

    @GetMapping
    public String  findAllProducts(Model model){
        model.addAttribute("product_list", productService.findAllProducts() );
        return "product_list";
    }

    @GetMapping("/find/{product_id}")
    public ResponseEntity<Product> findProductById(
            @PathVariable("product_id") Integer id
    ){
        return ResponseEntity.ok().body(productService.findProductById(id));
    }

    @GetMapping("/search/{p_name}")
    public ResponseEntity<List<Product>> findProductByName(
            @PathVariable("p_name") String name
    ){
        return ResponseEntity.ok().body(this.productService.findProductByName(name));
    }

    @GetMapping("/delete/{id}")
    public String delete(
            @PathVariable("id") Integer id
    ){
        this.productService.deleteProductById(id);
        return "redirect:/products";
    }
}
