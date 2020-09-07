package com.gabrielspassos.poc.stub;

import com.gabrielspassos.poc.entity.UserEntity;
import com.gabrielspassos.poc.enumerator.UserStatusEnum;

import java.time.LocalDateTime;

public class UserEntityStub {

    public static UserEntity create(Long id, UserStatusEnum status, String login, String email) {
        LocalDateTime dateTime = LocalDateTime.of(2020, 9, 7, 16, 14 , 50);

        UserEntity userEntity = new UserEntity();
        userEntity.setId(id);
        userEntity.setLogin(login);
        userEntity.setPassword("1234");
        userEntity.setEmail(email);
        userEntity.setStatus(status);
        userEntity.setCreationDateTime(dateTime);
        userEntity.setUpdateDateTime(dateTime);
        return userEntity;
    }
}
