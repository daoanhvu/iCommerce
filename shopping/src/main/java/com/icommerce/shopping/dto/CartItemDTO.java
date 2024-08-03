package com.icommerce.shopping.dto;

public class CartItemDTO {
    private Long id;
    private Long productId;
    private int quantity;

    public CartItemDTO() {}

    public CartItemDTO(Long id, Long productId, int quty) {
        this.id = id;
        this.productId = productId;
        this.quantity = quty;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long product) {
        this.productId = product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
