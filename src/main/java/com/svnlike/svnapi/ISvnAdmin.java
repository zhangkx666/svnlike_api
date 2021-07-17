package com.svnlike.svnapi;

import com.svnlike.svnapi.model.SvnRepository;

/**
 * svn admin interface
 *
 * @author zhangkx
 */
public interface ISvnAdmin {

    /**
     * createRepository a new repository
     *
     * @param svnRepository SvnRepository
     * @return SvnRepository
     */
    SvnRepository createRepository(SvnRepository svnRepository);

    /**
     * moveRepository repository
     *
     * @param oldRepoName old repository name
     * @param newRepoName new repository name
     */
    void moveRepository(String oldRepoName, String newRepoName);

    /**
     * deleteRepository repository
     *
     * @param repoName repository name
     */
    void deleteRepository(String repoName);

    /**
     * restart svnserve service
     */
    void restartSvnService();
}
