package com.svnlike.api.model.dto.repository.request;


import com.svnlike.api.model.dto.RequestDTO;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RepositoryConditionDTO extends RequestDTO {

    /**
     * Search keyword
     */
    private String keyword;

    /**
     * Page number
     */
    private Integer page;

    /**
     * Page size
     */
    private Integer pageSize;

    /**
     * Get trimmed keyword
     *
     * @return trimmed repository name
     */
    public String getKeyword() {
        return keyword == null ? "" : keyword.trim();
    }

    /**
     * Get page
     * @return page
     */
    public int getPage() {
        return page == null ? 1 : page;
    }

    /**
     * Get pageSize
     * @return pageSize
     */
    public int getPageSize() {
        return pageSize == null ? 15 : pageSize;
    }

    @Override
    public String toString() {
        return "page=" + getPage() + ",pageSize=" + getPageSize() + ",keyword=" + getKeyword();
    }
}
