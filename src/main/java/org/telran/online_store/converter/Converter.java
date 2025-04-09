package org.telran.online_store.converter;

public interface Converter<RequestDto, ResponseDto, Entity> {

    ResponseDto toDto(Entity entity);

    Entity toEntity(RequestDto dto);
}
