package com.marssvn.api.service.business.impl;

import com.marssvn.api.model.entity.User;
import com.marssvn.utils.dao.ICommonDAO;
import com.marssvn.utils.message.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class BaseService {
    /**
     * Common DAO
     */
    @Autowired
    ICommonDAO commonDAO;

    /**
     * Message i18n
     */
    @Autowired
    Message message;

    /**
     * Logger
     */
    Logger logger = LoggerFactory.getLogger("BusinessService");

    /**
     * user info
     */
    User uvo;

    public BaseService() {
        User uvo = new User();
        uvo.setId(1);
        uvo.setName("zhangkx");
        uvo.setRealName("张坤祥");
        this.uvo = uvo;
    }
}
