package com.alarm.parent.util;

/**
 * Created by tqyao.
 */
public enum ErrorCode {
    KEYWORD_IS_NULL(1000,"KEYWORD_IS_NULL","关键词为空"),
    ;


    private int status;
    private String code, message;

    private ErrorCode(int status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
