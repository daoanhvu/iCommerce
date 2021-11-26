package com.icommerce.shopping.service;

import com.icommerce.shopping.model.Product;
import com.icommerce.shopping.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
@Profile({"h2"})
public class DataInitializer {

    private final ProductRepository productRepository;

    @Autowired
    public DataInitializer(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @PostConstruct
    public void prepareTestData() {
        Product product1 = new Product();
        product1.setId(null);
        product1.setName("Young Lady Dress");
        product1.setBrand("Branch 1");
        product1.setPrice(15.0);
        product1.setCategory("Fashion");
        product1.setColour("Red");
        productRepository.save(product1);

        Product product2 = new Product();
        product2.setId(null);
        product2.setName("Baby Dress");
        product2.setBrand("Branch 1");
        product2.setPrice(5.0);
        product2.setCategory("Baby");
        product2.setColour("Yellow");
        productRepository.save(product2);

        Product product3 = new Product();
        product3.setId(null);
        product3.setName("Printer");
        product3.setBrand("HP");
        product3.setPrice(785.0);
        product3.setCategory("Office Supply");
        product3.setColour("White");
        productRepository.save(product3);
    }
}
