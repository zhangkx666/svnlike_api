package com.svnlike.api.model.entity;

import com.svnlike.svnapi.enums.ESvnProtocol;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * @author zhangkx
 */
@Getter
@Setter
public class Repository extends Entity {

    /**
     * ID
     */
    private Integer id;

    /**
     * project id
     */
    private Integer projectId;

    /**
     * user id
     */
    private Integer userId;

    /**
     * repository name
     */
    private String name;

    /**
     * repository url name
     */
    private String urlName;

    /**
     * local path
     */
    private String localPath;

    /**
     * svn url for checkout
     */
    private String svnUrl;

    /**
     * description
     */
    private String description;

    /**
     * protocol
     */
    private ESvnProtocol protocol;

    /**
     * visibility, 1: Authorized by subversion,  2: Website only, 3:Public
     */
    private Integer visibility;

    /**
     * created at
     */
    private Date createdAt;

    /**
     * updated at
     */
    private Date updatedAt;
}
