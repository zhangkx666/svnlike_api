package com.svnlike.svnapi.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.svnlike.utils.model.BaseBean;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class SvnLock extends BaseBean {

    /**
     * SvnLock token
     */
    private String token;

    /**
     * SvnLock owner
     */
    private String owner;

    /**
     * SvnLock comment
     */
    private String comment;

    /**
     * SvnLock created at
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdAt;
}
