package com.nikitoe.stockdividend.exception.impl;

import com.nikitoe.stockdividend.exception.AbstractException;
import org.springframework.http.HttpStatus;

public class EmptyTickerException extends AbstractException {

    @Override
    public int getStatusCode() {
        return HttpStatus.BAD_REQUEST.value();
    }

    @Override
    public String getMessage() {
        return "ticker 값이 비어있습니다.";
    }
}
