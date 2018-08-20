package com.marssvn.api.model.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class SVNFile extends Entity {

    /**
     * file type
     */
    private String type = "file";

    /**
     * file name
     */
    private String name;

    /**
     * file extension
     */
    private String extension;

    /**
     * file path
     */
    private String path;

    /**
     * full path
     */
    private String fullPath;

    /**
     * file size
     */
    private long size;

    /**
     * revision
     */
    private long revision;

    /**
     * author
     */
    private String author;

    /**
     * date
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updatedAt;

    /**
     * commit message
     */
    private String commitMessage;

    /**
     * lockOwner
     */
    private SVNLockInfo lock;
}
