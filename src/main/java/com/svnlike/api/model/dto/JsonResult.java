package com.svnlike.api.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.svnlike.utils.common.Message;
import com.svnlike.utils.enums.EMessageType;
import lombok.Getter;
import lombok.Setter;

/**
 * Success Result
 *
 * @author zhangkx
 */
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class JsonResult {

    /**
     * message type
     */
    @JsonIgnore
    private EMessageType type;

    /**
     * get type low case
     *
     * @return type low case
     */
    @JsonProperty("type")
    public String getTypeValue() {
        if (this.type == null) {
            this.type = EMessageType.SUCCESS;
        }
        return this.type.getValue();
    }

    /**
     * code
     */
    private String code;

    /**
     * message
     */
    private String message;

    /**
     * data: object
     */
    private Object data;

    /**
     * default
     */
    public JsonResult() {
    }

    /**
     * new JsonResult with Message
     *
     * @param message message
     */
    public JsonResult(String message) {
        this.message = message;
    }

    /**
     * new JsonResult with Message
     *
     * @param code    code
     * @param message message
     */
    public JsonResult(String code, String message) {
        this.code = code;
        this.message = message;
    }

    /**
     * new JsonResult with Message and data
     *
     * @param message Message
     * @param data    data
     */
    public JsonResult(String message, Object data) {
        this.message = message;
        this.data = data;
    }

    /**
     * new JsonResult with Message and data
     *
     * @param code    code
     * @param message Message
     * @param data    data
     */
    public JsonResult(String code, String message, Object data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    /**
     * new JsonResult with Message
     *
     * @param message Message
     */
    public JsonResult(Message message) {
        this.type = message.getType();
        this.message = message.toString();
    }

    /**
     * new JsonResult with Message and data
     *
     * @param data    data
     * @param message Message
     */
    public JsonResult(Message message, Object data) {
        this.code = message.getCode();
        this.type = message.getType();
        this.message = message.toString();
        this.data = data;
    }
}
