package com.svnlike.api.model.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class SVNFile extends Entity {

    /**
     * file name
     */
    private String name;

    /**
     * file extension
     */
    @JsonProperty("ext")
    private String extension;

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
     * file size
     */
    private long size;

    /**
     * revision
     */
    @JsonProperty("rev")
    private long revision;

    /**
     * author
     */
    private String author;

    /**
     * message (text file only)
     */
    private String content;

    /**
     * date
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updatedAt;

    /**
     * commit message
     */
    @JsonProperty("msg")
    private String commitMessage;

    /**
     * lockOwner
     */
    private SVNLockInfo lock;
}
