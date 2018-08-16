package com.marssvn.api.model.dto.repository;


import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class RepositoryInputDTO {

    /**
     * Repository name
     */
    @NotBlank(message = "{repository.name.blank}")
    private String name;

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
