package org.telran.online_store.converter;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.telran.online_store.dto.UserRegistrationRequest;
import org.telran.online_store.dto.UserRegistrationResponse;
import org.telran.online_store.entity.User;
import org.telran.online_store.enums.UserRole;

@Component
@RequiredArgsConstructor
public class UserRegistrationConverter implements Converter<UserRegistrationRequest, UserRegistrationResponse, User> {

    private final PasswordEncoder passwordEncoder;

    @Override
    public UserRegistrationResponse toDto(User user) {
        return UserRegistrationResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .phone(user.getPhone())
                .build();
    }

    @Override
    public User toEntity(UserRegistrationRequest request) {
        return User.builder()
                .name(request.name())
                .email(request.email())
                .phone(request.phone())
                .password(passwordEncoder.encode(request.password()))
                .role(UserRole.CLIENT)
                .build();
    }
}
