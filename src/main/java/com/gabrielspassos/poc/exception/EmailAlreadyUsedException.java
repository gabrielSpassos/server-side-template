package com.gabrielspassos.poc.exception;

public class EmailAlreadyUsedException extends BusinessException {

    public EmailAlreadyUsedException() {
        super("This email is already in use!");
    }
}
