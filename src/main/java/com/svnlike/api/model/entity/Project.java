package com.svnlike.api.model.entity;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class Project extends Entity {

    /**
     * id
     */
    private Integer id;

    /**
     * project name
     */
    private String name;

    /**
     * project name for url
     */
    private String urlName;

    /**
     * project description
     */
    private String description;

    /**
     * avatar
     */
    private String avatar;

    /**
     * avatar word
     */
    private String avatarWord;

    /**
     * avatar
     */
    private String avatarColor;

    /**
     * project owner, default is create user
     */
    private String owner;

    /**
     * create user
     */
    private String createdBy;

    /**
     * created at
     */
    private Date createdAt;

    /**
     * updated at
     */
    private Date updatedAt;

    /**
     * project visibility, 1: public,  2: private
     */
    private Integer visibility;

    /**
     * @return project visibility, default is 2: private
     */
    public Integer getVisibility() {
        if (this.visibility == null) {
            this.visibility = 2;
        }
        return this.visibility;
    }
}
