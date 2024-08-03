package com.icommerce.shopping.service.impl;

import com.icommerce.shopping.common.Constants;
import com.icommerce.shopping.dto.CartDTO;
import com.icommerce.shopping.dto.CartItemDTO;
import com.icommerce.shopping.dto.ServiceResponse;
import com.icommerce.shopping.model.Cart;
import com.icommerce.shopping.model.CartItem;
import com.icommerce.shopping.repository.CartItemRepository;
import com.icommerce.shopping.repository.CartRepository;
import com.icommerce.shopping.service.ShoppingCartService;
import com.icommerce.shopping.util.DTOMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {

    private final Logger LOG = LoggerFactory.getLogger(ShoppingCartServiceImpl.class);

    private final CartItemRepository cartItemRepository;
    private final CartRepository cartRepository;

    @Autowired
    public ShoppingCartServiceImpl(CartItemRepository cartItemRepository, CartRepository cartRepository) {
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
    }

    /**
     * Add a list of items into active cart of a given user
     * @param userId the user
     * @param items list of items
     * @return return the cart's id
     */
    @Override
    public ServiceResponse<Long> addToCart(Long userId, List<CartItemDTO> items) {
        ServiceResponse<Long> resp = new ServiceResponse<>();
        Optional<Cart> optCart = cartRepository.findByUserIdAndPaid(userId, false);
        if(optCart.isEmpty()) {
            Cart cart = new Cart();
            cart.setPaid(false);
            cart.setUserId(userId);
            cart.setOrderDateTime(OffsetDateTime.now());
            cart.setItems(items.stream().map(DTOMapping::toCartItemEntity).collect(Collectors.toList()));
            Cart savedCard = cartRepository.save(cart);
            resp.setResult(savedCard.getId());
        } else {
            Cart cart = optCart.get();
            if(cart.getItems() != null) {
                List<CartItem> existingItems = cart.getItems();
                Map<Long, CartItem> existingItemsMap = existingItems.stream().collect(Collectors.toMap(CartItem::getProductId, Function.identity()));
                for(CartItemDTO itm: items) {
                    if(existingItemsMap.containsKey(itm.getProductId())) {
                        CartItem existItem = existingItemsMap.get(itm.getProductId());
                        int existQty = existItem.getQuantity();
                        existItem.setQuantity(existQty + itm.getQuantity());
                    } else {
                        existingItems.add(DTOMapping.toCartItemEntity(itm));
                    }
                }
                Cart savedCard = cartRepository.save(cart);
                resp.setResult(savedCard.getId());
                resp.setServiceCode(Constants.SERVICE_CODE_DUPLICATE);
                resp.setServiceMessage("This product is already in cart!");
                resp.setResult(savedCard.getId());
            } else {
                cart.setItems(items.stream().map(DTOMapping::toCartItemEntity).collect(Collectors.toList()));
                Cart savedCard = cartRepository.save(cart);
                resp.setResult(savedCard.getId());
            }
        }
        return resp;
    }

    @Override
    public ServiceResponse<Long> updateCart(CartDTO request) {
        ServiceResponse<Long> resp = new ServiceResponse<>();
        Optional<Cart> optCart = cartRepository.findById(request.getId());
        if(optCart.isEmpty()) {
            resp.setHttpStatus(HttpStatus.NOT_FOUND.value());
            resp.setServiceCode(Constants.SERVICE_CODE_ERROR);
            LOG.debug("Cart with id {} not found", request.getId());
            return resp;
        }

        Cart existingCart = optCart.get();

        if (existingCart.isPaid()) {
            resp.setHttpStatus(HttpStatus.BAD_REQUEST.value());
            resp.setServiceCode(Constants.SERVICE_BAD_REQUEST);
            LOG.debug("This cart {} is completed and can not be updated.", request.getId());
            return resp;
        }

        Cart updatingCart = DTOMapping.toCartEntity(request);
        existingCart.setItems(updatingCart.getItems());
        existingCart.setOrderDateTime(updatingCart.getOrderDateTime());
        cartRepository.save(existingCart);
        resp.setResult(existingCart.getId());
        resp.setServiceCode(Constants.SERVICE_CODE_OK);
        resp.setHttpStatus(HttpStatus.OK.value());
        return resp;
    }

    @Override
    public ServiceResponse<CartDTO> getCart(Long cartId) {
        ServiceResponse<CartDTO> resp = new ServiceResponse<>();
        Optional<Cart> optCart = cartRepository.findById(cartId);
        if(optCart.isEmpty()) {
            resp.setHttpStatus(HttpStatus.NOT_FOUND.value());
            resp.setServiceCode(Constants.SERVICE_CODE_ERROR);
            LOG.debug("Cart {} not found", cartId);
            return resp;
        }
        resp.setServiceCode(Constants.SERVICE_CODE_OK);
        resp.setHttpStatus(HttpStatus.OK.value());
        resp.setResult(DTOMapping.toCartDTO(optCart.get()));

        return resp;
    }

    @Override
    public ServiceResponse<CartDTO> getUnpaidCartByUser(Long userId) {
        ServiceResponse<CartDTO> resp = new ServiceResponse<>();
        Optional<Cart> optCart = cartRepository.findByUserIdAndPaid(userId, false);
        if(optCart.isEmpty()) {
            resp.setHttpStatus(HttpStatus.NOT_FOUND.value());
            resp.setServiceCode(Constants.SERVICE_CODE_ERROR);
            LOG.debug("Cart not found for user {}", userId);
            return resp;
        }
        resp.setServiceCode(Constants.SERVICE_CODE_OK);
        resp.setHttpStatus(HttpStatus.OK.value());
        resp.setResult(DTOMapping.toCartDTO(optCart.get()));

        return resp;
    }
}
