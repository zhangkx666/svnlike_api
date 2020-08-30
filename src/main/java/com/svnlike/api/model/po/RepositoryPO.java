package com.svnlike.api.model.po;

import com.svnlike.utils.enums.ESvnProtocol;
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
     * user id
     */
    private Integer userId;

    /**
     * user name
     */
    private String userName;

    /**
     * repository title
     */
    private String title;

    /**
     * repository name
     */
    private String name;

    /**
     * repository path
     */
    private String path;

    /**
     * description
     */
    private String description;

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
