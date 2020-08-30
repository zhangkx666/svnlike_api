package com.svnlike.api.service.impl;

import com.svnlike.api.model.entity.User;
import com.svnlike.utils.dao.ICommonDAO;
import com.svnlike.utils.common.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author zhangkx
 */
class BaseService {

    /**
     * Common DAO
     */
    @Autowired
    ICommonDAO commonDAO;

    /**
     * Message utils i18n
     */
    @Autowired
    Message m;

    /**
     * Logger
     */
    Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * user info
     */
    User uvo;

    BaseService() {
        User uvo = new User();
        uvo.setId(1);
        uvo.setName("zhangkx");
        uvo.setRealName("张坤祥");
        this.uvo = uvo;
    }
}
