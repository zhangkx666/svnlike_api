package com.svnlike.utils.common;

import com.svnlike.utils.enums.EMessageType;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

/**
 * @author zhangkx
 */
@Component
@Getter
@Setter
public class Message {

    /**
     * MessageSource
     */
    private final MessageSource messageSource;

    @Autowired
    public Message(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    /**
     * code
     */
    private String code;

    /**
     * message type: success, info, warning, error
     */
    private EMessageType type;

    /**
     * args
     */
    private String[] args;

    /**
     * Set error type message
     *
     * @param code message code
     * @param args parameters
     * @return Message
     */
    public Message error(String code, String... args) {
        this.type = EMessageType.ERROR;
        this.code = code;
        this.args = args;
        return this;
    }

    /**
     * Set info type message
     *
     * @param code message code
     * @param args parameters
     * @return Message
     */
    public Message info(String code, String... args) {
        this.type = EMessageType.INFO;
        this.code = code;
        this.args = args;
        return this;
    }

    /**
     * Set warning type message
     *
     * @param code message code
     * @param args parameters
     * @return Message
     */
    public Message warning(String code, String... args) {
        this.type = EMessageType.WARNING;
        this.code = code;
        this.args = args;
        return this;
    }

    /**
     * Set success type message
     *
     * @param code message code
     * @param args parameters
     * @return Message
     */
    public Message success(String code, String... args) {
        this.type = EMessageType.SUCCESS;
        this.code = code;
        this.args = args;
        return this;
    }

    /**
     * get message value
     *
     * @param code message code
     * @param args parameters
     * @return Message
     */
    public String val(String code, String... args) {
        this.code = code;
        this.args = args;
        return this.getLocalMessage();
    }

    /**
     * Get real message by code
     *
     * @return String realMessage
     */
    private String getLocalMessage() {
        if (this.args == null) {
            return this.code;
        }
        return this.messageSource.getMessage(this.code, this.args, this.code, LocaleContextHolder.getLocale());
    }

    /**
     * to string
     *
     * @return String
     */
    @Override
    public String toString() {
        return this.getLocalMessage();
    }
}
