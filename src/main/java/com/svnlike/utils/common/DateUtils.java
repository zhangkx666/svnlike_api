package com.svnlike.utils.common;

import com.svnlike.utils.exception.SvnApiException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * @author zhangkx
 */
public class DateUtils {

    /**
     * parse svn date
     *
     * @param str date string like 2021-03-13T15:11:38.491465Z
     * @return Date
     */
    public static Date parseSvnDate(String str) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        df.setTimeZone(TimeZone.getTimeZone("UTC"));
        try {
            return df.parse(str);
        } catch (ParseException e) {
            throw new SvnApiException("Convert date failed.");
        }
    }
}
