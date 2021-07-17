package com.svnlike.svnapi.model;

import com.svnlike.utils.common.StringUtils;
import com.svnlike.svnapi.enums.ERepositoryType;
import com.svnlike.svnapi.enums.ESvnProtocol;
import lombok.Getter;
import lombok.Setter;

/**
 * svn repository
 *
 * @author zhangkx
 */
@Getter
@Setter
public class SvnRepository {

    /**
     * repository UUID
     */
    private String uuid;

    /**
     * repository name
     */
    private String name;

    /**
     * project url name
     */
    private String projectUrlName;

    /**
     * svn root path
     */
    private String svnRootPath;

    /**
     * repository type
     */
    private ERepositoryType repositoryType;

    /**
     * svn protocol
     */
    private ESvnProtocol svnProtocol;

    /**
     * head headRevision
     */
    private long headRevision;

    /**
     * admin account
     */
    private SvnUser adminUser;

    /**
     * get svn root path
     * if svn root path is blank, return System.getProperty("user.home") + "/svn"
     *
     * @return String svn root path
     */
    public String getSvnRootPath() {
        if (StringUtils.isBlank(svnRootPath)) {
            svnRootPath = System.getProperty("user.home") + "/svn";
            if (!StringUtils.isBlank(projectUrlName)) {
                svnRootPath += "/" + projectUrlName;
            }
        }
        return svnRootPath;
    }

    /**
     * Get repository root path
     *
     * @return Root Path
     */
    public String getRepoRootPath() {
        return getSvnRootPath() + "/" + name;
    }
}
