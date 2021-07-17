package com.svnlike.utils.exception;

/**
 * SvnApiException
 *
 * @author zhangkx
 */
public class SvnApiException extends RuntimeException {
    /**
     * errorCode
     */
    private String errorCode;

    /**
     * get errorCode
     *
     * @return string
     */
    public String getErrorCode() {
        return this.errorCode;
    }

    /**
     * New SvnApiException by message string
     *
     * @param message message
     */
    public SvnApiException(String message) {
        super(message);
    }

    /**
     * New SvnApiException by errorCode and message
     *
     * @param errorCode error code
     * @param message   message
     */
    public SvnApiException(String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }
}
