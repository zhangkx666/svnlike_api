package com.svnlike.utils.common;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;

/**
 * @author zhangkx
 */
@Component
public class UtilsX {

    private static String encryptAppendString;

    @Value("${svnlike.encryptAppendString}")
    public void setEncryptAppendString(String str) {
        encryptAppendString = str;
    }

    /**
     * md5 by spring DigestUtils
     *
     * @param str string for encrypt
     * @return md5 string
     */
    public static String md5(String str) {
        System.out.println("encryptAppendString: " + encryptAppendString);
        return DigestUtils.md5DigestAsHex((encryptAppendString + str).getBytes());
    }
}
