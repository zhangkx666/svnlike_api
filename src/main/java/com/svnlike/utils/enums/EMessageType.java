package com.svnlike.utils.enums;

/**
 * svn protocol
 *
 * @author zhangkx
 */
public enum EMessageType {
    /**
     * success
     */
    SUCCESS("success"),

    /**
     * info
     */
    INFO("info"),

    /**
     * error
     */
    ERROR("error"),

    /**
     * warning
     */
    WARNING("warn");

    private String value;

    EMessageType(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }
}
