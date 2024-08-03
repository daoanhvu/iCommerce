package com.icommerce.shopping.controller;

import com.icommerce.shopping.dto.PageableContent;
import com.icommerce.shopping.dto.ServiceResponse;
import com.icommerce.shopping.model.Product;
import com.icommerce.shopping.service.impl.ProductServiceImpl;
import com.icommerce.shopping.dto.PageableRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductServiceImpl productService;

    @Autowired
    public ProductController(ProductServiceImpl productService) {
        this.productService = productService;
    }

    @RequestMapping(value = "/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE,
            method = RequestMethod.GET)
    public ResponseEntity<ServiceResponse<Product>> getById(@PathVariable("id") Long id) {
        ServiceResponse<Product> result = productService.getProductById(id);
        return new ResponseEntity<>(result, HttpStatus.valueOf(result.getHttpStatus()));
    }

    @RequestMapping(path = "/search", method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ServiceResponse<PageableContent<Product>>> searchProduct(@RequestBody PageableRequest req) {
        ServiceResponse<PageableContent<Product>> response = productService.searchProduct(req);
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getHttpStatus()));
    }
}
