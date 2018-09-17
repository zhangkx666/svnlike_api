package com.marssvn.api.model.dto.repository.request;


import com.marssvn.api.model.dto.RequestDTO;
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

    @Override
    public String toString() {
        return path;
    }
}
