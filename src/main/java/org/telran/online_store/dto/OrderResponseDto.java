package org.telran.online_store.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import org.telran.online_store.enums.DeliveryMethod;
import org.telran.online_store.enums.OrderStatus;
import java.time.LocalDateTime;
import java.util.List;

@Builder
public record OrderResponseDto(

        @Schema(description = "Order id", example = "1")
        Long id,

        @Schema(description = "Contact phone", example = "+222222222")
        String contactPhone,

        @Schema(description = "Delivery address", example = "Backer street, 221b")
        String deliveryAddress,

        @Schema(description = "Delivery method", example = "Per post")
        DeliveryMethod deliveryMethod,

        @Schema(description = "Order status", example = "PAYMENT_PENDING")
        OrderStatus status,

        @Schema(description = "Time when the order has been created", example = "2025-05-06T18:51:18.356189")
        LocalDateTime createdAt,

        @Schema(description = "Time when the order has been updated", example = "2025-05-07T10:51:18.356189")
        LocalDateTime updatedAt,

        @Schema(description = "List of order items", example = """
                                                                    "items": [
                                                                        {
                                                                        "productId": 1,
                                                                        "quantity": 1,
                                                                        "priceAtPurchase": 15.99
                                                                        }
                                                                      ]
                                                                    """)

        List<OrderItemResponseDto> items) {
}
