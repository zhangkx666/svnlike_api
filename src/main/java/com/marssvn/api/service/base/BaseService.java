package com.marssvn.api.service.base;

import com.marssvn.utils.message.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class BaseService {

    /**
     * Message i18n
     */
    @Autowired
    Message message;

    /**
     * Logger
     */
    static Logger logger = LoggerFactory.getLogger("BaseService");
}
