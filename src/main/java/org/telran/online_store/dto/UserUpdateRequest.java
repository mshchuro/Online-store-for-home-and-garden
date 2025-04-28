package org.telran.online_store.dto;

import lombok.Data;
import org.telran.online_store.enums.UserRole;

@Data
public class UserUpdateRequest {

    private String name;

    private String phone;

    private UserRole role;
}
