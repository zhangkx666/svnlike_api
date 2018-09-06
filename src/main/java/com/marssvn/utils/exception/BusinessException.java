package com.marssvn.utils.exception;

import com.marssvn.utils.message.Message;
import org.jetbrains.annotations.NotNull;

public class BusinessException extends RuntimeException{

    /**
     * errorCode
     */
    private String errorCode;

    /**
     * get errorCode
     * @return string
     */
    public String getErrorCode() {
        return this.errorCode;
    }

    /**
     * New BusinessException by message string
     * @param message message
     */
    public BusinessException(String message) {
        super(message);
    }

    /**
     * New BusinessException by IMessage.class
     *
     * @param message IMessage
     */
    public BusinessException(@NotNull Message message) {
        super(message.toString());
        this.errorCode = message.getCode();
    }
}
