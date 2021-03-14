package com.svnlike.api.model.dto.repository.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.svnlike.api.model.dto.ResponseDTO;
import com.svnlike.svnapi.enums.ESvnProtocol;
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
     * project url name
     */
    private String projectUrlName;

    /**
     * user id
     */
    private Integer userId;

    /**
     * user name
     */
    private String userName;

    /**
     * repository name
     */
    private String name;

    /**
     * repository url name
     */
    private String urlName;

    /**
     * local path
     */
    private String localPath;

    /**
     * svn url
     */
    private String svnUrl;

    /**
     * description
     */
    private String description;

    /**
     * visibility,  1: Authorized by subversion,  2: Website only, 3:Public
     */
    private Integer visibility;

    /**
     * protocol
     */
    @JsonIgnore
    private ESvnProtocol protocol;

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
