package com.marssvn.api.model.dto.repository;

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
     * directory
     */
    private SvnDirectory directory;
}
