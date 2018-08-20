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
     * commit message
     */
    private String commitMessage;

    /**
     * files
     */
    private List<SvnFile> files;

    /**
     * sub directories
     */
    private List<SvnDirectory> children;
}
