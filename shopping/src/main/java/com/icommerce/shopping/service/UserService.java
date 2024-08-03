package com.icommerce.shopping.service;

import com.icommerce.shopping.dto.ServiceResponse;
import com.icommerce.shopping.model.Cart;

public interface UserService {

    ServiceResponse<Cart> getCart(Long cartId);
}
