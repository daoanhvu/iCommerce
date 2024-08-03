package com.icommerce.shopping.controller;

import com.icommerce.shopping.dto.CartDTO;
import com.icommerce.shopping.dto.ServiceResponse;
import com.icommerce.shopping.service.ShoppingCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {
    private final ShoppingCartService cartService;

    @Autowired
    public UserController(ShoppingCartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping(path = "/{userId}/carts")
    public ResponseEntity<?> getShoppingCartByUserId(@PathVariable("userId") Long userId) {
        ServiceResponse<CartDTO> responseContent = cartService.getUnpaidCartByUser(userId);
        return new ResponseEntity<>(responseContent, HttpStatus.valueOf(responseContent.getHttpStatus()));
    }
}
