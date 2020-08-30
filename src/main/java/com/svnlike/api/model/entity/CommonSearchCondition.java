package com.svnlike.api.model.entity;


import com.svnlike.utils.common.StringUtils;
import lombok.Getter;
import lombok.Setter;

/**
 * @author zhangkx
 */
@Getter
@Setter
public class CommonSearchCondition extends Entity {

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
     * Order by
     */
    private String orderBy;

    /**
     * @return trimmed repository name
     */
    public String getKeyword() {
        return keyword == null ? "" : keyword.trim();
    }

    /**
     * @return page
     */
    public int getPage() {
        return page == null ? 1 : page;
    }

    /**
     * @return pageSize
     */
    public int getPageSize() {
        return pageSize == null ? 0 : pageSize;
    }

    /**
     * @return off set
     */
    public int getOffset() {
        return (this.getPage() - 1) * this.getPageSize();
    }

    /**
     * @return clean order by string
     */
    public String getOrderBy() {
        if (this.orderBy == null) {
            this.orderBy = "";
        }
        return StringUtils.replaceSpecialChars(this.orderBy);
    }
}
