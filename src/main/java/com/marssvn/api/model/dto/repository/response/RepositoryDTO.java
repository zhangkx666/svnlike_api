package com.marssvn.api.model.dto.repository.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.marssvn.api.model.dto.ResponseDTO;
import com.marssvn.utils.enums.ESVNProtocol;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class RepositoryDTO extends ResponseDTO {

    /**
     * ID
     */
    private Integer id;

    /**
     * project id
     */
    private Integer projectId;

    /**
     * project name
     */
    private String projectName;

    /**
     * user id
     */
    private Integer userId;

    /**
     * user name
     */
    private String userName;

    /**
     * repository title
     */
    private String title;

    /**
     * repository name
     */
    private String name;

    /**
     * repository path
     */
    private String path;

    /**
     * description
     */
    private String description;

    /**
     * protocol
     */
    @JsonIgnore
    private ESVNProtocol protocol;

    /**
     * protocol lower
     */
    @JsonProperty("protocol")
    private String protocolLower;

    /**
     * created at
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdAt;

    /**
     * updated at
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updatedAt;

    /**
     * get protocol lower
     *
     * @return protocol lower
     */
    public String getProtocolLower() {
        return this.protocol.getValue();
    }
}
