package com.marssvn.api.model.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class User extends Entity {
    /**
     * user id
     */
    private Integer id;

    /**
     * user name
     */
    private String name;

    /**
     * user real name
     */
    private String realName;
}
