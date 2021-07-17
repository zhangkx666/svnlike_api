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
public class SvnFileDto extends BaseBean {

    /**
     * file name
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
     * mime type
     */
    @JsonProperty("mime")
    private String mimeType;

    /**
     * file path
     */
    private String path;

    /**
     * parent path
     */
    private String parentPath;

    /**
     * message (text file only)
     */
    private String content;

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
