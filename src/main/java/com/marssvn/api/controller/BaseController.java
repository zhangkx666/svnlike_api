package com.marssvn.api.controller;

import com.marssvn.utils.message.Message;
import org.springframework.beans.factory.annotation.Autowired;

public class BaseController {

    /**
     * Message
     */
    @Autowired
    Message message;
}
