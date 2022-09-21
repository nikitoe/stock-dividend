package com.nikitoe.stockdividend.exception;

import lombok.Builder;
import lombok.Data;

// 에러 발생시 던져줄 model class
@Data
@Builder
public class ErrorResponse {

    private int code;
    private String message;

}
