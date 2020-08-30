package com.svnlike.api.model.dto.project.request;

import com.svnlike.api.model.entity.CommonSearchCondition;
import lombok.Getter;
import lombok.Setter;

/**
 * @author zhangkx
 */
@Getter
@Setter
public class ProjectConditionDTO extends CommonSearchCondition {

    /**
     * for select list, only return id and urlName
     */
    private Boolean forSelect;

    public boolean getForSelect() {
        return this.isForSelect();
    }

    public boolean isForSelect() {
        if (this.forSelect == null) {
            this.forSelect = false;
        }
        return this.forSelect;
    }
}
