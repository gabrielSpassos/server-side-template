package com.gabrielspassos.poc.builder.entity;

import com.gabrielspassos.poc.controller.request.UserRequest;
import com.gabrielspassos.poc.entity.UserEntity;
import com.gabrielspassos.poc.enumerator.UserStatusEnum;

import java.time.LocalDateTime;
import java.util.Objects;

public class UserEntityBuilder {

    public static UserEntity build(UserRequest request, UserStatusEnum status) {
        LocalDateTime now = LocalDateTime.now();

        return UserEntity.builder()
                .login(request.getLogin())
                .password(request.getPassword())
                .email(request.getEmail())
                .status(status)
                .creationDateTime(now)
                .updateDateTime(now)
                .build();
    }

    public static UserEntity build(UserEntity entity, UserRequest request) {
        LocalDateTime now = LocalDateTime.now();
        String login = Objects.nonNull(request.getLogin()) ? request.getLogin() : entity.getLogin();
        String password = Objects.nonNull(request.getPassword()) ? request.getPassword() : entity.getPassword();
        String email = Objects.nonNull(request.getEmail()) ? request.getEmail() : entity.getEmail();

        return UserEntity.builder()
                .id(entity.getId())
                .login(login)
                .password(password)
                .email(email)
                .status(entity.getStatus())
                .creationDateTime(entity.getCreationDateTime())
                .updateDateTime(now)
                .build();
    }
}
