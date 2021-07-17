package com.svnlike.api.model.dto.repository.response;

import com.svnlike.api.model.dto.ResponseDTO;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RepositoryTreeDTO extends ResponseDTO {

    /**
     * repository uuid
     */
    private String uuid;

    /**
     * repository root
     */
    private String root;

    /**
     * repository protocol
     */
    private String protocol;

    /**
     * directory
     */
    private SvnTreeDto tree;
}
