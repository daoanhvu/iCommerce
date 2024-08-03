package com.icommerce.shopping.service;

import com.icommerce.shopping.dto.CartDTO;
import com.icommerce.shopping.dto.CartItemDTO;
import com.icommerce.shopping.dto.ServiceResponse;

import java.util.List;

public interface ShoppingCartService {

    ServiceResponse<Long> addToCart(Long userId, List<CartItemDTO> items);
    ServiceResponse<Long> updateCart(CartDTO request);
    ServiceResponse<CartDTO> getCart(Long cartId);
    ServiceResponse<CartDTO> getUnpaidCartByUser(Long userId);
}
