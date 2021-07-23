package com.spring.aop.advice;

public interface Advice {

    void invoke(Object[] args) throws Exception;
}
