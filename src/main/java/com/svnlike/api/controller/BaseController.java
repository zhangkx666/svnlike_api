package com.svnlike.api.controller;

import com.svnlike.utils.common.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;

@CrossOrigin
public class BaseController {

    /**
     * Message
     */
    @Autowired
    Message m;
}
