package com.marssvn.utils.common;

public class StringUtils {

    /**
     * check if the str is empty
     *
     * @param str string
     * @return boolean
     */
    public static boolean isEmpty(String str) {
        return str == null || str.length() == 0;
    }

    /**
     * check if the str is not empty
     *
     * @param str string
     * @return boolean
     */
    public static boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }

    /**
     * check if the str is blank
     *
     * @param str string
     * @return boolean
     */
    public static boolean isBlank(String str) {
        return str == null || str.trim().length() == 0;
    }

    /**
     * check if the str is not blank
     *
     * @param str string
     * @return boolean
     */
    public static boolean isNotBlank(String str) {
        return !isBlank(str);
    }
}
