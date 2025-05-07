package org.telran.online_store.dto;

import org.telran.online_store.enums.UserRole;

public record UserUpdateRequestDto(
        String name,

        String phone
) {}
