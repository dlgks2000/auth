package com.lab.auth.domain.user;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapper {

    public static UserMapper Instance = Mappers.getMapper(UserMapper.class);

    User toModel(UserEntity entity);
}
