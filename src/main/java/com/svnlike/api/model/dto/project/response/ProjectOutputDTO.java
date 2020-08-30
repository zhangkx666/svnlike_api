package com.svnlike.api.model.dto.project.response;

import com.svnlike.api.model.dto.ResponseDTO;
import lombok.Getter;
import lombok.Setter;

/**
 * @author zhangkx
 */
@Getter
@Setter
public class ProjectOutputDTO extends ResponseDTO {

    /**
     * id
     */
    private Integer id;

    /**
     * project name
     */
    private String name;

    /**
     * project name for url
     */
    private String urlName;

    /**
     * project description
     */
    private String description;

    /**
     * avatar
     */
    private String avatar;

    /**
     * avatar word
     */
    private String avatarWord;

    /**
     * avatar color
     */
    private String avatarColor;

    /**
     * project visibility, 1: public,  2: private
     */
    private Integer visibility;
}
