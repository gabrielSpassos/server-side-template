package com.gabrielspassos.poc.stub;

import com.gabrielspassos.poc.controller.request.UserRequest;

public class UserRequestStub {

    public static UserRequest create(String email, String login, String password) {
        UserRequest userRequest = new UserRequest();
        userRequest.setEmail(email);
        userRequest.setLogin(login);
        userRequest.setPassword(password);
        return userRequest;
    }
}
