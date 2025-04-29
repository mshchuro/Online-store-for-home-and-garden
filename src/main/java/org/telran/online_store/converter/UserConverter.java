package org.telran.online_store.converter;

import org.springframework.stereotype.Component;
import org.telran.online_store.dto.UserUpdateRequestDto;
import org.telran.online_store.dto.UserUpdateResponseDto;
import org.telran.online_store.entity.User;

@Component
public class UserConverter implements Converter<UserUpdateRequestDto, UserUpdateResponseDto, User>{

    @Override
    public UserUpdateResponseDto toDto(User user) {
        return UserUpdateResponseDto
                .builder()
                .id(user.getId())
                .name(user.getName())
                .phone(user.getPhone())
                .email(user.getEmail())
                .build();
    }

    @Override
    public User toEntity(UserUpdateRequestDto userUpdateRequestDto) {
        return User
                .builder()
                .name(userUpdateRequestDto.name())
                .phone(userUpdateRequestDto.phone())
                .build();
    }
}
