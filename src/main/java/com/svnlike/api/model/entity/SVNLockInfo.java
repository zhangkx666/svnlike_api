package com.svnlike.api.model.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class SVNLockInfo extends Entity {
    /**
     * lock owner
     */
    private String owner;

    /**
     * lock comment
     */
    private String comment;

    /**
     * lock created at
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdAt;
}
