package com.marssvn.api.model.po;

import com.marssvn.utils.enums.ESVNProtocol;
import com.marssvn.utils.model.BaseBean;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

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
    private ESVNProtocol protocol;

    /**
     * created at
     */
    private Date createdAt;

    /**
     * updated at
     */
    private Date updatedAt;
}
