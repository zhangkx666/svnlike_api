package com.marssvn.utils.message;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

@Component
public class Message {

    /**
     * MessageSource
     */
    @Autowired
    private MessageSource messageSource;

    /**
     * success
     */
    public static final String MESSAGE_TYPE_SUCCESS = "success";

    /**
     * info
     */
    public static final String MESSAGE_TYPE_INFO = "info";

    /**
     * warning
     */
    public static final String MESSAGE_TYPE_WARNING = "warning";

    /**
     * error
     */
    public static final String MESSAGE_TYPE_ERROR = "error";

    /**
     * code
     */
    private String code;

    /**
     * message type: success, info, warning, error
     */
    private String type;

    /**
     * args
     */
    private String[] args;

    /**
     * Get code
     * @return code
     */
    public String getCode() {
        return this.code;
    }

    /**
     * Get message type
     * @return message type
     */
    public String getType() {
        return this.type;
    }

    /**
     * Set error type message
     * @param code message code
     * @param args parameters
     * @return Message
     */
    public Message error(String code, String... args) {
        this.type = MESSAGE_TYPE_ERROR;
        this.code = code;
        this.args = args;
        return this;
    }

    /**
     * Set info type message
     * @param code message code
     * @param args parameters
     * @return Message
     */
    public Message info(String code, String... args) {
        this.type = MESSAGE_TYPE_INFO;
        this.code = code;
        this.args = args;
        return this;
    }

    /**
     * Set warning type message
     * @param code message code
     * @param args parameters
     * @return Message
     */
    public Message warning(String code, String... args) {
        this.type = MESSAGE_TYPE_WARNING;
        this.code = code;
        this.args = args;
        return this;
    }

    /**
     * Set success type message
     * @param code message code
     * @param args parameters
     * @return Message
     */
    public Message success(String code, String... args) {
        this.type = MESSAGE_TYPE_SUCCESS;
        this.code = code;
        this.args = args;
        return this;
    }

    /**
     * Get local message
     * @param code string
     * @param args String[]
     * @return String
     */
    public String lang(String code, String... args) {
        this.code = code;
        this.args = args;
        return getRealMessage();
    }

    /**
     * Get real message by code
     * @return String realMessage
     */
    private String getRealMessage() {
        try {
            return this.messageSource.getMessage(this.code, this.args, LocaleContextHolder.getLocale());
        } catch (NoSuchMessageException e) {
            return this.code;
        }
    }

    /**
     * to string
     * @return String
     */
    @Override
    public String toString() {
        return getRealMessage();
    }
}
