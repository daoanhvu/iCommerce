package com.icommerce.shopping.controller;

import com.icommerce.shopping.dto.CartDTO;
import com.icommerce.shopping.dto.ServiceResponse;
import com.icommerce.shopping.service.ShoppingCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/carts")
public class ShoppingCartController {

    private final ShoppingCartService cartService;

    @Autowired
    public ShoppingCartController(ShoppingCartService cartService) {
        this.cartService = cartService;
    }

    @PutMapping(path = "/{cartId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ServiceResponse<Long>> updateCart(@PathVariable("cartId") Long cartId, @RequestBody CartDTO request) {
        ServiceResponse<Long> response = cartService.updateCart(request);
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getHttpStatus()));
    }

    @GetMapping(path = "/{cartId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ServiceResponse<CartDTO>> getCart(@PathVariable("cartId") Long cartId) {
        ServiceResponse<CartDTO> response = cartService.getCart(cartId);
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getHttpStatus()));
    }
}
