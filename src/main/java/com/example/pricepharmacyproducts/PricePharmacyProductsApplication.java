package com.example.pricepharmacyproducts;

import com.google.ortools.Loader;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class PricePharmacyProductsApplication {

	public static void main(String[] args) {

		Loader.loadNativeLibraries();
		SpringApplication.run(PricePharmacyProductsApplication.class, args);
	}

}
