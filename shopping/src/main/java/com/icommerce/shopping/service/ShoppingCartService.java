package com.icommerce.shopping.service;

import com.icommerce.shopping.common.Constants;
import com.icommerce.shopping.dto.ServiceResponse;
import com.icommerce.shopping.dto.UpdateCartItemRequest;
import com.icommerce.shopping.model.Cart;
import com.icommerce.shopping.model.CartItem;
import com.icommerce.shopping.model.Product;
import com.icommerce.shopping.repository.CartItemRepository;
import com.icommerce.shopping.repository.CartRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Optional;

@Service
public class ShoppingCartService {

    private final Logger LOG = LoggerFactory.getLogger(ShoppingCartService.class);

    private final CartItemRepository cartItemRepository;
    private final CartRepository cartRepository;

    @Autowired
    public ShoppingCartService(CartItemRepository cartItemRepository, CartRepository cartRepository) {
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
    }

    public ServiceResponse<Cart> addToCart(String userSessionId, Product product) {
        ServiceResponse<Cart> resp = new ServiceResponse<>();
        Optional<Cart> optCart = cartRepository.findByUserSessionId(userSessionId);
        if(optCart.isEmpty()) {
            CartItem item = new CartItem();
            item.setProduct(product);
            item.setQuantity(1);

            Cart cart = new Cart();
            cart.setUserSessionId(userSessionId);
            cart.setOrderDate(LocalDate.now());
            cart.setItems(Arrays.asList(item));
            Cart savedCard = cartRepository.save(cart);
            resp.setResult(savedCard);
            LOG.debug("Product {} has been add to cart sessionId = {}", product.getId(), userSessionId);
        } else {
            Cart cart = optCart.get();
            Optional<CartItem> existingOpt = cart.getItems()
                    .stream().filter(x -> x.getProduct().getId().equals(product.getId())).findFirst();
            if(existingOpt.isPresent()) {
                resp.setServiceCode(Constants.SERVICE_CODE_DUPLICATE);
                resp.setServiceMessage("This product is already in cart!");
                resp.setResult(cart);
            } else {
                CartItem item = new CartItem();
                item.setProduct(product);
                item.setQuantity(1);
                cart.getItems().add(item);
                Cart savedCard = cartRepository.save(cart);
                resp.setResult(savedCard);
            }
        }
        return resp;
    }

    public ServiceResponse<CartItem> updateCart(UpdateCartItemRequest request) {
        ServiceResponse<CartItem> resp = new ServiceResponse<>();
        Optional<CartItem> optCart = cartItemRepository.findById(request.getCartItemId());
        if(optCart.isEmpty()) {
            resp.setHttpStatus(HttpStatus.NOT_FOUND.value());
            resp.setServiceCode(Constants.SERVICE_CODE_ERROR);
            LOG.debug("Item {} not found", request.getCartItemId());
            return resp;
        }
        CartItem cartItem = optCart.get();
        cartItem.setQuantity(request.getQuantity());
        cartItemRepository.save(cartItem);
        resp.setResult(cartItem);
        resp.setServiceCode(Constants.SERVICE_CODE_OK);
        resp.setHttpStatus(HttpStatus.OK.value());
        return resp;
    }

    public ServiceResponse<Cart> getCart(String userSessionId) {
        ServiceResponse<Cart> resp = new ServiceResponse<>();
        Optional<Cart> optCart = cartRepository.findByUserSessionId(userSessionId);
        if(optCart.isEmpty()) {
            resp.setHttpStatus(HttpStatus.NOT_FOUND.value());
            resp.setServiceCode(Constants.SERVICE_CODE_ERROR);
            LOG.debug("Cart for sessionId {} not found", userSessionId);
            return resp;
        }
        resp.setServiceCode(Constants.SERVICE_CODE_OK);
        resp.setHttpStatus(HttpStatus.OK.value());
        resp.setResult(optCart.get());

        return resp;
    }
}
