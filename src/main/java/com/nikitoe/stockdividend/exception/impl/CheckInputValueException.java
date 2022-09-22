package com.nikitoe.stockdividend.exception.impl;

import com.nikitoe.stockdividend.exception.AbstractException;
import org.springframework.http.HttpStatus;

public class CheckInputValueException extends AbstractException {

    @Override
    public int getStatusCode() {
        return HttpStatus.BAD_REQUEST.value();
    }

    @Override
    public String getMessage() {
        return "올바르지 않은 입력값입니다.";
    }
}
