package com.marssvn.api.model.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
public class SvnFile extends Entity {

    /**
     * file name
     */
    private String name;

    /**
     * file type
     */
    private String type = "file";

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
    private Date date;

    /**
     * lockOwner
     */
    private String lockOwner;

    /**
     * commit message
     */
    private String commitMessage;
}
