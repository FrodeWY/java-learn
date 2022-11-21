package com.learn.handler;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.async.AsyncRequestTimeoutException;

/**
 * @author wangyang
 * @date 2022-11-15
 * @desc
 */
@RestControllerAdvice
public class AsyncRequestTimeoutHandler {
    @ResponseStatus(HttpStatus.REQUEST_TIMEOUT)
    @ExceptionHandler(AsyncRequestTimeoutException.class)
    public String asyncRequestTimeoutHandler(AsyncRequestTimeoutException e){
        System.out.println("异步请求超时");
        return "304";
    }
}
