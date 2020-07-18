package com.gabrielspassos.poc.exception;

public class NotFoundUserException extends BusinessException {

    public NotFoundUserException() {
        super("Not found customer");
    }
}
