package org.telran.online_store.dto;

import lombok.Builder;
import org.telran.online_store.enums.UserRole;

@Builder
public record UserUpdateResponseDto(
    Long id,

    String name,

    String email,

    String phone) {}
