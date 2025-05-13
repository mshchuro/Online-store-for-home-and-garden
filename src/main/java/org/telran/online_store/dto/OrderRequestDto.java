package org.telran.online_store.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;

import java.util.List;

@Builder
public record OrderRequestDto(

        @Schema(description = "List of order items", example = "\"items\": [\n" +
                                                               "      {\n" +
                                                               "        \"productId\": 1,\n" +
                                                               "        \"quantity\": 1,\n" +
                                                               "        \"priceAtPurchase\": 15.99\n" +
                                                               "      }]")
        List<OrderItemRequestDto> items,

        @NotBlank(message = "Delivery Address must not be blank")
        @Size(min = 3, max = 255, message = "Product name must be at least 3 and at most 255 characters")
        @Schema(description = "Delivery Address", example = "Backer street, 221b")
        String deliveryAddress,

        @NotBlank(message = "Delivery Method must not be blank")
        @Size(min = 3, max = 255, message = "Delivery Method must be at least 3 and at most 255 characters")
        @Schema(description = "Delivery Method", example = "Per post")
        String deliveryMethod,

        @NotBlank(message = "Contact Phone must not be blank")
        @Pattern(regexp = "^\\+\\d{10,15}$", message = "Phone number must be in international format, e.g. +1234567890")
        @Schema(description = "Contact Phone", example = "+1234567890")
        String contactPhone
) {
}
