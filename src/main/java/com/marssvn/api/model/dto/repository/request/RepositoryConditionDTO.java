package com.marssvn.api.model.dto.repository.request;


import com.marssvn.api.model.dto.RequestDTO;
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
        return keyword == null ? "" : keyword.trim();
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

    @Override
    public String toString() {
        return page + keyword;
    }
}
