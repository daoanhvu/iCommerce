package com.icommerce.shopping.controller;

import com.icommerce.shopping.common.Constants;
import com.icommerce.shopping.dto.ServiceResponse;
import com.icommerce.shopping.dto.UpdateCartItemRequest;
import com.icommerce.shopping.model.Cart;
import com.icommerce.shopping.model.CartItem;
import com.icommerce.shopping.model.Product;
import com.icommerce.shopping.service.ShoppingCartService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cart")
public class ShoppingCartController {

    private final ShoppingCartService cartService;

    @Autowired
    public ShoppingCartController(ShoppingCartService cartService) {
        this.cartService = cartService;
    }

    @RequestMapping(path = "/add", method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ServiceResponse<Cart>> addToCart(@RequestHeader("user-session") String userSessionId, @RequestBody Product product) {
        ServiceResponse<Cart> response = cartService.addToCart(userSessionId, product);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @RequestMapping(path = "/update", method = RequestMethod.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ServiceResponse<CartItem>> updateCart(@RequestHeader("user-session") String userSessionId,
                                                                @RequestBody UpdateCartItemRequest request) {
        ServiceResponse<CartItem> response;
        //This is a simple security check
        if(StringUtils.isBlank(userSessionId) ||
            StringUtils.isBlank(request.getUserSessionId()) ||
                !userSessionId.equals(request.getUserSessionId())) {
            response = new ServiceResponse<>();
            response.setServiceMessage("Bad request");
            response.setServiceCode(Constants.SERVICE_BAD_REQUEST);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        response = cartService.updateCart(request);
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getHttpStatus()));
    }

    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ServiceResponse<Cart>> getCart(@RequestHeader("user-session") String userSessionId) {
        ServiceResponse<Cart> response = cartService.getCart(userSessionId);
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getHttpStatus()));
    }
}
