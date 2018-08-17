package com.marssvn.api.model.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
public class SvnDirectory extends Entity {

    /**
     * directory name
     */
    private String name;

    /**
     * file type
     */
    private String type = "dir";

    /**
     * svn directory path
     */
    private String path;

    /**
     * full path
     */
    private String fullPath;

    /**
     * sub directories
     */
    private List<SvnDirectory> children;

    /**
     * files
     */
    private List<SvnFile> files;

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
}
