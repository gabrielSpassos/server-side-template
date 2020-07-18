package com.gabrielspassos.poc.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public abstract class BusinessException extends RuntimeException{

    private String message;

}
