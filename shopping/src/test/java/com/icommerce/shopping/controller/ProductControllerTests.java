package com.icommerce.shopping.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.icommerce.shopping.common.Constants;
import com.icommerce.shopping.dto.PageableContent;
import com.icommerce.shopping.dto.PageableRequest;
import com.icommerce.shopping.dto.ServiceResponse;
import com.icommerce.shopping.model.Product;
import com.icommerce.shopping.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ProductControllerTests {

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ProductController productController;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void contextLoads() {
        assertNotNull(productController);
    }

    public List<Product> initData() {
        List<Product> products = new ArrayList<>();

        Product product1 = new Product();
        product1.setId(null);
        product1.setName("Laptop M");
        product1.setBrand("Branch M");
        product1.setPrice(1200.0);
        product1.setCategory("Office Supply");
        product1.setColour("Grey");
        Product savedProduct = productRepository.save(product1);
        products.add(savedProduct);

        Product product2 = new Product();
        product2.setId(null);
        product2.setName("Laptop M");
        product2.setBrand("Branch M");
        product2.setPrice(1200.0);
        product2.setCategory("Office Supply");
        product2.setColour("Grey");
        savedProduct = productRepository.save(product2);
        products.add(savedProduct);

        Product product3 = new Product();
        product3.setId(null);
        product3.setName("Laptop M");
        product3.setBrand("Branch M");
        product3.setPrice(1200.0);
        product3.setCategory("Office Supply");
        product3.setColour("Grey");
        savedProduct = productRepository.save(product3);
        products.add(savedProduct);

        Product product4 = new Product();
        product4.setId(null);
        product4.setName("Laptop M");
        product4.setBrand("Branch M");
        product4.setPrice(1200.0);
        product4.setCategory("Office Supply");
        product4.setColour("Grey");
        savedProduct = productRepository.save(product4);
        products.add(savedProduct);

        Product product5 = new Product();
        product5.setId(null);
        product5.setName("Laptop M");
        product5.setBrand("Branch M");
        product5.setPrice(1200.0);
        product5.setCategory("Office Supply");
        product5.setColour("Grey");
        savedProduct = productRepository.save(product5);
        products.add(savedProduct);

        return products;
    }

    @Test
    public void testGetById_Found() throws Exception {
        List<Product> products = initData();
        final String expectedName = products.get(0).getName();

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/products/" + products.get(0).getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String resultStr = result.getResponse().getContentAsString();
        ServiceResponse<Product> response = objectMapper.readValue(resultStr,
                new TypeReference<ServiceResponse<Product>>(){});
        assertNotNull(response);
        assertEquals(Constants.SERVICE_CODE_OK, response.getServiceCode());
        assertNotNull(response.getResult());
        assertEquals(expectedName, response.getResult().getName());
    }

    @Test
    public void testGetById_NotFound() throws Exception {
        long productId = 1009L;
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/products/" + productId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andReturn();

        String resultStr = result.getResponse().getContentAsString();
        ServiceResponse<Product> response = objectMapper.readValue(resultStr,
                new TypeReference<ServiceResponse<Product>>(){});
        assertNotNull(response);
        assertEquals(Constants.SERVICE_CODE_NOT_FOUND, response.getServiceCode());
    }

    @Test
    public void testSearchProduct() throws Exception {
        initData();
        PageableRequest searchRequest = new PageableRequest();
        searchRequest.setPage(1);
        searchRequest.setSize(10);

        final String requestText = objectMapper.writeValueAsString(searchRequest);
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/products/search")
                        .content(requestText)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        String resultStr = result.getResponse().getContentAsString();
        ServiceResponse<PageableContent<Product>> response = objectMapper.readValue(resultStr,
                new TypeReference<ServiceResponse<PageableContent<Product>>>(){});
        assertNotNull(response);
        assertEquals(5, response.getResult().getTotalElements());
    }
}
