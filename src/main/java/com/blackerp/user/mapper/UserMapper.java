package com.blackerp.user.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import com.blackerp.user.dto.UserResponse;
import com.blackerp.user.dto.UserUpdateRequest;
import com.blackerp.user.entity.User;

@Mapper
public interface UserMapper {

    UserResponse toResponse(User user);

    void updateFromRequest(UserUpdateRequest request, @MappingTarget User user);
}
