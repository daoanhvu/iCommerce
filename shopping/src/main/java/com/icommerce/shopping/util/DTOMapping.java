package com.icommerce.shopping.util;

import com.icommerce.shopping.dto.CartDTO;
import com.icommerce.shopping.dto.CartItemDTO;
import com.icommerce.shopping.model.Cart;
import com.icommerce.shopping.model.CartItem;
import org.springframework.lang.NonNull;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public final class DTOMapping {

    public static Cart toCartEntity(@NonNull CartDTO cartDTO) {
        List<CartItemDTO> items = cartDTO.getItems();
        List<CartItem> itemEntities = null;
        if(items != null) {
            itemEntities = items.stream().filter(Objects::nonNull).map(DTOMapping::toCartItemEntity).collect(Collectors.toList());
        }
        return new Cart(cartDTO.getId(),
                cartDTO.getUserId(), cartDTO.getOrderDateTime(), itemEntities, cartDTO.isPaid());
    }

    public static CartItem toCartItemEntity(@NonNull CartItemDTO itemDto) {
        return new CartItem(itemDto.getId(), itemDto.getProductId(), itemDto.getQuantity());
    }

    public static CartDTO toCartDTO(@NonNull Cart cart) {
        List<CartItem> items = cart.getItems();
        List<CartItemDTO> itemList = null;
        if(items != null) {
            itemList = items.stream().filter(Objects::nonNull).map(DTOMapping::toCartItemDTO).collect(Collectors.toList());
        }
        return new CartDTO(cart.getId(),
                cart.getUserId(), cart.getOrderDateTime(), itemList, cart.isPaid());
    }

    public static CartItemDTO toCartItemDTO(@NonNull CartItem itemEntity) {
        return new CartItemDTO(itemEntity.getId(), itemEntity.getProductId(), itemEntity.getQuantity());
    }
}
