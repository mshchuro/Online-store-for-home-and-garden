package org.telran.online_store.dto;

import lombok.Builder;

public record UserRegistrationRequest(
        String name,
        String email,
        String phone,
        String password
) {
}
