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

    private Long productId;

    @Test
    public void contextLoads() {
        assertNotNull(productController);
    }

    public void initData() {
        Product product1 = new Product();
        product1.setId(null);
        product1.setName("Laptop M");
        product1.setBrand("Branch M");
        product1.setPrice(1200.0);
        product1.setCategory("Office Supply");
        product1.setColour("Grey");
        Product savedProduct = productRepository.save(product1);

        productId = savedProduct.getId();
    }

    @Test
    public void testGetById_Found() throws Exception {
        final String name = "Laptop M";
        Product product1 = new Product();
        product1.setId(null);
        product1.setName("Laptop M");
        product1.setBrand("Branch M");
        product1.setPrice(1200.0);
        product1.setCategory("Office Supply");
        product1.setColour("Grey");
        Product savedProduct = productRepository.save(product1);
        productId = savedProduct.getId();

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/products/" + productId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String resultStr = result.getResponse().getContentAsString();
        ServiceResponse<Product> response = objectMapper.readValue(resultStr,
                new TypeReference<ServiceResponse<Product>>(){});
        assertNotNull(response);
        assertEquals(Constants.SERVICE_CODE_OK, response.getServiceCode());
        assertNotNull(response.getResult());
        assertEquals(name, response.getResult().getName());
    }

    @Test
    public void testGetById_NotFound() throws Exception {
        productId = 999L;
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
    }
}
