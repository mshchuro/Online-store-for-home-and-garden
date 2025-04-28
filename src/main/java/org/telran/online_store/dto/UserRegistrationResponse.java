package org.telran.online_store.dto;

import lombok.Builder;

@Builder
public record UserRegistrationResponse(
        Long id,
        String name,
        String email,
        String phone
) {
}
