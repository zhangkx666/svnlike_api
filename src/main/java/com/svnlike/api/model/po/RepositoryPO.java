package com.svnlike.api.model.po;

import com.svnlike.svnapi.enums.ESvnProtocol;
import com.svnlike.api.model.BaseBean;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * @author zhangkx
 */
@Getter
@Setter
public class RepositoryPO extends BaseBean {

    /**
     * ID
     */
    private Integer id;

    /**
     * project id
     */
    private Integer projectId;

    /**
     * project name
     */
    private String projectName;

    /**
     * project url name
     */
    private String projectUrlName;

    /**
     * user id
     */
    private Integer userId;

    /**
     * user name
     */
    private String userName;

    /**
     * repository name
     */
    private String name;

    /**
     * repository url name
     */
    private String urlName;

    /**
     * repository local path
     */
    private String localPath;

    /**
     * repository svn url
     */
    private String svnUrl;

    /**
     * description
     */
    private String description;

    /**
     * visibility,  1: Authorized by subversion,  2: Website only, 3:Public
     */
    private Integer visibility;

    /**
     * protocol
     */
    private ESvnProtocol protocol;

    /**
     * created at
     */
    private Date createdAt;

    /**
     * updated at
     */
    private Date updatedAt;
}
