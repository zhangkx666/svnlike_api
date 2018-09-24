package com.marssvn.api.service.base;

import com.marssvn.api.model.dto.repository.response.RepositoryTreeDTO;
import org.tmatesoft.svn.core.SVNURL;

/**
 * Repository Base Service
 */
public interface IRepositoryBaseService {

    /**
     * Create empty directory
     *
     * @param svnurls SVNURL
     * @param comment String
     */
    void createDirectory(SVNURL[] svnurls, String comment);

    /**
     * create svn repository on server
     *
     * @param repositoryName repository name
     * @return SVNURL
     */
    SVNURL createSvnRepository(String repositoryName);

    /**
     * get repository tree
     *
     * @param repositoryPath repository path
     * @param repositoryName repository name
     * @param path           path
     * @return SVNDirectory
     */
    RepositoryTreeDTO getRepositoryTree(String repositoryPath, String repositoryName, String path);
}
