package org.telran.online_store.dto;

import lombok.Data;

@Data
public class AddToCartRequest {
    private Long productId;

    private Integer quantity;
}
