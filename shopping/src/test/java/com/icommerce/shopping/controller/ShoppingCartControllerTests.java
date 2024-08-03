package com.icommerce.shopping.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.icommerce.shopping.common.Constants;
import com.icommerce.shopping.dto.CartDTO;
import com.icommerce.shopping.dto.ServiceResponse;
import com.icommerce.shopping.model.Cart;
import com.icommerce.shopping.model.CartItem;
import com.icommerce.shopping.model.Product;
import com.icommerce.shopping.repository.ProductRepository;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ShoppingCartControllerTests {

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    private Long productId;
    private Long cartId;
    private Long userSessionId;
    private Long toBeUpdatedCartItemId;

    private void initDataForTesting() {
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
    @Order(1)
    public void testAddToCart() throws Exception {
        initDataForTesting();

        userSessionId = 1L;
        Product product = new Product();
        product.setId(productId);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/cart/add")
                        .content(objectMapper.writeValueAsString(product))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("user-session", userSessionId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();

        String resultStr = result.getResponse().getContentAsString();
        ServiceResponse<Cart> response = objectMapper.readValue(resultStr, new TypeReference<>() {});
        assertNotNull(response);
        assertEquals(Constants.SERVICE_CODE_OK, response.getServiceCode());
        assertNotNull(response.getResult());
        assertTrue(response.getResult().getItems().size() > 0);
        cartId = response.getResult().getId();
        toBeUpdatedCartItemId = response.getResult().getItems().get(0).getId();
    }

    @Test
    @Order(2)
    public void testUpdateCart() throws Exception {
        initDataForTesting();
        Product product = new Product();
        product.setId(productId);

        userSessionId = 2L;

        MvcResult addCartResult = mockMvc.perform(MockMvcRequestBuilders.post("/cart/add")
                        .content(objectMapper.writeValueAsString(product))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("user-session", userSessionId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();
        ServiceResponse<Cart> response = objectMapper.readValue(addCartResult.getResponse().getContentAsString(),
                new TypeReference<>() {});
        assertNotNull(response);
        cartId = response.getResult().getId();
        toBeUpdatedCartItemId = response.getResult().getItems().get(0).getId();

        CartDTO request = new CartDTO();
        request.setId(cartId);
//        request.setQuantity(10);
        request.setUserId(userSessionId);
//        request.setCartItemId(toBeUpdatedCartItemId);
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.put("/cart/update")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("user-session", userSessionId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String resultStr = result.getResponse().getContentAsString();
        ServiceResponse<CartItem> updatingResponse = objectMapper.readValue(resultStr, new TypeReference<>() {});
        assertNotNull(updatingResponse);
        assertEquals(Constants.SERVICE_CODE_OK, updatingResponse.getServiceCode());
    }

    @Test
    @Order(3)
    public void testGetCart() throws Exception {
        initDataForTesting();
        Product product = new Product();
        product.setId(productId);

        userSessionId = 3L;

        MvcResult addCartResult = mockMvc.perform(MockMvcRequestBuilders.post("/cart/add")
                        .content(objectMapper.writeValueAsString(product))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("user-session", userSessionId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();
        ServiceResponse<Cart> response = objectMapper.readValue(addCartResult.getResponse().getContentAsString(),
                new TypeReference<>() {});
        assertNotNull(response);
        cartId = response.getResult().getId();
        toBeUpdatedCartItemId = response.getResult().getItems().get(0).getId();

       MvcResult resultGet = mockMvc.perform(MockMvcRequestBuilders.get("/cart")
                        .header("user-session", userSessionId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String resultStr = resultGet.getResponse().getContentAsString();
        ServiceResponse<Cart> updatingResponse = objectMapper.readValue(resultStr, new TypeReference<>() {});
        assertNotNull(updatingResponse);
        assertEquals(Constants.SERVICE_CODE_OK, updatingResponse.getServiceCode());
    }
}
