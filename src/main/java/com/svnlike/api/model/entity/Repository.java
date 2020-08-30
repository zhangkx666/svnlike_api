package com.svnlike.api.model.entity;

import com.svnlike.utils.enums.ESvnProtocol;
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
     * domain or IP
     */
    private String domain;

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

    /**
     * get repository url
     * @return String
     */
    public String getUrl() {
        if (ESvnProtocol.FILE == this.protocol) {
            return this.protocol.getPrefix() + this.path;
        } else {
            return this.protocol.getPrefix() + this.domain + "/" + this.name;
        }
    }
}
