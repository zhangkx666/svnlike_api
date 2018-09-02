package com.marssvn.api.model.dto.repository.request;


import com.marssvn.api.model.dto.RequestDTO;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class RepositoryInputDTO extends RequestDTO {

    /**
     * project id
     */
    private Integer projectId;

    /**
     * Repository name
     */
    @NotBlank(message = "{repository.name.blank}")
    private String name;

    /**
     * Repository title
     */
    @NotBlank(message = "{repository.title.blank}")
    private String title;

    /**
     * Repository description
     */
    private String description;

    /**
     * Auto make trunk, branches, tags directory
     */
    private Boolean autoMakeDir;

    /**
     * Get repository name
     *
     * @return trimmed repository name
     */
    public String getName() {
        return name == null ? "" : name.trim();
    }
}
