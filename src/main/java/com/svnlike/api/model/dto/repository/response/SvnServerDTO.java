package com.svnlike.api.model.dto.repository.response;

import com.svnlike.api.model.BaseBean;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * @author zhangkx
 */
@Getter
@Setter
public class SvnServerDTO extends BaseBean {

    /**
     * ID
     */
    private Integer id;

    /**
     * 域名
     */
    private String domain;

    /**
     * IP
     */
    private String ip;

    /**
     * 描述
     */
    private String description;

    /**
     * 是否可用
     */
    private Integer isEnabled;

    /**
     * 创建时间
     */
    private Date createdAt;

    /**
     * 更新时间
     */
    private Date updatedAt;
}
