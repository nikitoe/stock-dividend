package com.nikitoe.stockdividend.exception.impl;

import com.nikitoe.stockdividend.exception.AbstractException;
import org.springframework.http.HttpStatus;

public class NoUserException extends AbstractException {

    @Override
    public int getStatusCode() {
        return HttpStatus.BAD_REQUEST.value();
    }

    @Override
    public String getMessage() {
        return "존재하지 않는 회원 입니다.";
    }
}
