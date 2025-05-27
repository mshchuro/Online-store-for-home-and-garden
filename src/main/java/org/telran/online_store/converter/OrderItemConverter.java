package org.telran.online_store.converter;

import org.springframework.stereotype.Component;
import org.telran.online_store.dto.OrderItemRequestDto;
import org.telran.online_store.dto.OrderItemResponseDto;
import org.telran.online_store.entity.OrderItem;
import org.telran.online_store.entity.Product;
import org.telran.online_store.service.ProductService;
import java.math.BigDecimal;

@Component
public class OrderItemConverter implements Converter<OrderItemRequestDto, OrderItemResponseDto, OrderItem> {

    private final ProductService productService;

    public OrderItemConverter(ProductService productService) {
        this.productService = productService;
    }

    @Override
    public OrderItemResponseDto toDto(OrderItem orderItem) {
        return OrderItemResponseDto
                .builder()
                .productId(orderItem.getProduct().getId())
                .quantity(orderItem.getQuantity())
                .priceAtPurchase(orderItem.getPriceAtPurchase())
                .build();
    }

    @Override
    public OrderItem toEntity(OrderItemRequestDto orderItemRequestDto) {
        Product product = productService.getById(orderItemRequestDto.productId());
        BigDecimal discount = product.getDiscountPrice() == null ? BigDecimal.ZERO : product.getDiscountPrice();
        int quantity = orderItemRequestDto.quantity();
        return OrderItem.builder()
                .product(product)
                .quantity(quantity)
                .priceAtPurchase(product.getPrice().subtract(discount).multiply(new BigDecimal(quantity)))
                .build();
    }
}