package com.marssvn.api.controller;

import com.marssvn.utils.message.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;

@CrossOrigin
public class BaseController {

    /**
     * Message
     */
    @Autowired
    Message message;
}
