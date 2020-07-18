package com.gabrielspassos.poc.builder.dto;

import com.gabrielspassos.poc.dto.UserDTO;
import com.gabrielspassos.poc.entity.UserEntity;

public class UserDTOBuilder {

    public static UserDTO build(UserEntity entity) {
        return UserDTO.builder()
                .id(entity.getId())
                .login(entity.getLogin())
                .password(entity.getPassword())
                .email(entity.getEmail())
                .creationDateTime(entity.getCreationDateTime())
                .updateDateTime(entity.getUpdateDateTime())
                .build();
    }
}
