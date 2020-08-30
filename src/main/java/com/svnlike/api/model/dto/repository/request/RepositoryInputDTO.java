package com.svnlike.api.model.dto.repository.request;


import com.svnlike.api.model.dto.RequestDTO;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * @author zhangkx
 */
@Getter
@Setter
public class RepositoryInputDTO extends RequestDTO {

    /**
     * Repository name
     */
    @NotBlank(message = "{repository.name.blank}")
    private String name;

    /**
     * project id
     */
    @NotNull(message = "{repository.projectId.null}")
    @Min(value = 1, message = "{repository.projectId.notAvailable")
    private Integer projectId;

    /**
     * Repository title
     */
    @Pattern(regexp = "^[a-z0-9]+$", message = "{repository.urlName.notAvailable}")
    private String urlName;

    /**
     * Repository description
     */
    private String description;

    /**
     * Auto make trunk, branches, tags directory
     */
    private Boolean initWithTrunk;
}
