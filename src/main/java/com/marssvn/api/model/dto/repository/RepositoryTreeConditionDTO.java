package com.marssvn.api.model.dto.repository;


import com.marssvn.api.model.dto.RequestDTO;
import com.marssvn.utils.common.StringUtils;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RepositoryTreeConditionDTO extends RequestDTO {

    /**
     * path
     */
    private String path;


    /**
     * Get trimmed path
     *
     * @return trimmed repository path
     */
    public String getPath() {
        return path == null ? "" : path.trim();
    }
}
