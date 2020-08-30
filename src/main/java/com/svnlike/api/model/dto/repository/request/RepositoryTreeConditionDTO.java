package com.svnlike.api.model.dto.repository.request;


import com.svnlike.api.model.dto.RequestDTO;
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
     * get full path
     */
    private Boolean getAll;

    /**
     * Get trimmed path
     *
     * @return string path
     */
    public String getPath() {
        return path == null ? "" : path.trim();
    }

    /**
     * get all
     * @return boolean
     */
    public Boolean getGetAll() {
        return getAll == null ? false : getAll;
    }

    @Override
    public String toString() {
        return "path:" + getPath() + ",includeAll=" + getGetAll();
    }
}
