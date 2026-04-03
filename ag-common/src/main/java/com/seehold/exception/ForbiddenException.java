package com.seehold.exception;

/**
 * ForbiddenException是一个编程中常见的异常类型，
 * 表示对某个资源的访问被系统明确禁止。
 */
public class ForbiddenException extends BusinessException {

    public ForbiddenException(String message) {
        super(403, message);
    }
}