package com.marssvn.api.model.dto.repository;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RepositoryConditionDTO {

    /**
     * Search keyword
     */
    private String keyword;

    /**
     * Page number
     */
    private int page;

    /**
     * Page size
     */
    private int pageSize;

    /**
     * Get trimmed keyword
     *
     * @return trimmed repository name
     */
    public String getKeyword() {
        return keyword == null ? null : keyword.trim();
    }

    /**
     * Get page
     * @return page
     */
    public int getPage() {
        return page == 0 ? 1 : page;
    }

    /**
     * Get pageSize
     * @return pageSize
     */
    public int getPageSize() {
        return pageSize == 0 ? 15 : pageSize;
    }
}
