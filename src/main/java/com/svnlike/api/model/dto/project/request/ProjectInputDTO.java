package com.svnlike.api.model.dto.project.request;

import com.svnlike.api.model.dto.RequestDTO;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

/**
 * @author zhangkx
 */
@Getter
@Setter
public class ProjectInputDTO extends RequestDTO {

    /**
     * project name
     */
    @NotBlank(message = "{project.name.blank}")
    private String name;

    /**
     * project name for url
     */
    @NotBlank(message = "{project.urlName.blank}")
    private String urlName;

    /**
     * project description
     */
    private String description;

    /**
     * project visibility, 1: public,  2: private
     */
    private Integer visibility;

    /**
     * avatar
     */
    private String avatar;

    /**
     * avatar word
     */
    private String avatarWord;

    /**
     * avatar
     */
    private String avatarColor;
}
