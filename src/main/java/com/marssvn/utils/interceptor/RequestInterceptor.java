package com.marssvn.utils.interceptor;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Enumeration;
import java.util.Map;

@Component
public class RequestInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {

        // url
        if ("GET".equals(request.getMethod()) && request.getQueryString() != null) {
            System.out.println("\n" + request.getMethod() + " " + request.getRequestURL().toString() +
                    "?" + request.getQueryString());
        } else {
            System.out.println("\n" + request.getMethod() + " " + request.getRequestURL().toString());
        }

        // headers
        Enumeration<String> requestHeader = request.getHeaderNames();
        System.out.println("\n-------------- headers --------------");
        while (requestHeader.hasMoreElements()) {
            String headerKey = requestHeader.nextElement();
            System.out.println(headerKey + ": " + request.getHeader(headerKey));
        }

        // cookies
        if (request.getCookies() != null) {
            System.out.println("\n-------------- cookies --------------");
            for (Cookie cookie : request.getCookies()) {
                System.out.println(cookie.getName() + ": " + cookie.getValue());
            }
        }

        // parameters
        System.out.println("\n-------------- parameters --------------");
//            String body = IOUtils.toString(request.getInputStream(), "UTF-8");
//            System.out.println("body: " + body);
        Map<String, String[]> requestMsg = request.getParameterMap();
        for (String key : requestMsg.keySet()) {
            for (int i = 0; i < requestMsg.get(key).length; i++) {
                System.out.println(key + ": " + requestMsg.get(key)[i]);
            }
        }
        System.out.println("\n");
        return true;
    }
}