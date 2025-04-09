package org.telran.online_store.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.telran.online_store.dto.UserUpdateRequest;
import org.telran.online_store.entity.User;

@Mapper(componentModel = "spring")
public interface UserMapper {
    void updateUserFromDto(UserUpdateRequest dto, @MappingTarget User entity);
}
