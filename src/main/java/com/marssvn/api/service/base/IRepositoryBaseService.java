package com.marssvn.api.service.base;

import com.marssvn.api.model.entity.SVNFile;
import com.marssvn.api.model.entity.SVNTreeItem;
import org.tmatesoft.svn.core.SVNURL;

/**
 * Repository Base Service
 * @author zhangkx
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
     * @param getALl         get all
     * @return SVNDirectory
     */
    SVNTreeItem getRepositoryTree(String repositoryPath, String repositoryName, String path, Boolean getALl);

    SVNFile getRepositoryFile(String repositoryPath, String filePath);
}
