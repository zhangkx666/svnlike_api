package com.marssvn.api.model.dto.repository.response;

import com.marssvn.api.model.dto.ResponseDTO;
import com.marssvn.api.model.entity.SvnDirectory;
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
    private SvnDirectory tree;
}
