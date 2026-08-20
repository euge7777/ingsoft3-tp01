package com.agustin.server.mappers;

import com.agustin.server.domain.entities.User;
import com.agustin.server.dtos.responses.UserDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDTO toDto(User user);
    User toEntity(UserDTO userDto);
}
