package com.svnlike.api.service;

import com.svnlike.api.model.entity.SvnServer;

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
