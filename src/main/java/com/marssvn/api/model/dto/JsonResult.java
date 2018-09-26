package com.marssvn.api.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.marssvn.utils.message.Message;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

/**
 * RestController response dto
 * @author zhangkx
 */
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class JsonResult {

    /**
     * New JsonResult
     */
    public JsonResult() {}

    /**
     * New JsonResult with message
     * @param message message
     */
    public JsonResult(@NotNull Message message) {
        setMessage(message.getType(), message.toString());
    }

    /**
     * New JsonResult with data
     *
     * @param data data
     */
    public JsonResult(Object data) {
        this.data = data;
    }

    /**
     * status  1: success(default)   0:failed
     */
    private int status = 1;

    /**
     * error code
     */
    private String errorCode;

    /**
     * message
     */
    private HashMap<String, String> message;

    /**
     * data: object or list
     */
    private Object data;

    /**
     * Set message
     * @param type message type
     * @param message message content
     */
    public void setMessage(String type, String message) {
        HashMap<String, String> messageMap = new HashMap<>(2);
        messageMap.put("type", type);
        messageMap.put("content", message);
        this.message = messageMap;
    }

    /**
     * Set message
     * @param message Message
     */
    public void setMessage(Message message) {
        setMessage(message.getType(), message.toString());
    }

    /**
     * Set message
     * @param messageMap HashMap
     */
    public void setMessage(HashMap<String, String > messageMap) {
        this.message = messageMap;
    }
}
