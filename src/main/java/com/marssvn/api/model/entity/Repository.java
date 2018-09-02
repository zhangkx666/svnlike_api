package com.marssvn.api.model.entity;

import com.marssvn.utils.enums.ESVNProtocol;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

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
