package com.marssvn.api.service;

import com.marssvn.api.model.entity.SvnServer;

import java.util.List;

/**
 * Repository Service
 *
 * @author zhangkx
 */
public interface ISvnServerService {

    /**
     * Get repository list
     *
     * @return List
     */
    List<SvnServer> getSvnServerList();

}
