package com.svnlike.api.model.dto.repository.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.svnlike.svnapi.model.SvnLock;
import com.svnlike.utils.model.BaseBean;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * @author zhangkx
 */
@Getter
@Setter
public class SvnTreeDto extends BaseBean {

    /**
     * kind
     */
    private String kind = "dir";

    /**
     * name
     */
    private String name;

    /**
     * extension (file only)
     */
    @JsonProperty("ext")
    private String extension;

    /**
     * size (file only)
     */
    private long size;

    /**
     * revision
     */
    @JsonProperty("rev")
    private long commitRevision;

    /**
     * author
     */
    @JsonProperty("author")
    private String commitAuthor;

    /**
     * date
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date commitDate;

    /**
     * lockOwner (file only)
     */
    private SvnLock lock;
}
