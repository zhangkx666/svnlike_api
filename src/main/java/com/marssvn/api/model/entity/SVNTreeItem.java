package com.marssvn.api.model.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
public class SVNTreeItem extends Entity {

    /**
     * type
     */
    private String type = "dir";

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
     * path
     */
    private String path;

    /**
     * parent path
     */
    private String parentPath;

    /**
     * full path
     */
//    private String fullPath;

    /**
     * size (file only)
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
     * lockOwner (file only)
     */
    private SVNLockInfo lock;

    /**
     * sub directories (directory only)
     */
    private List<SVNTreeItem> children;
}
