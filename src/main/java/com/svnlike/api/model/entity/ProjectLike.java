package com.svnlike.api.model.entity;

import lombok.Getter;
import lombok.Setter;


/**
 * @author zhangkx
 */
@Getter
@Setter
public class ProjectLike extends Entity {

    /**
     * id
     */
    private Integer projectId;

    /**
     * project name
     */
    private Integer userId;

    public ProjectLike(){}
    public ProjectLike(Integer projectId, Integer userId) {
        this.projectId = projectId;
        this.userId = userId;
    }
}
