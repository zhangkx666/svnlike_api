package com.svnlike.api.service;

import com.svnlike.api.model.dto.repository.request.RepositoryConditionDTO;
import com.svnlike.api.model.dto.repository.request.RepositoryInputDTO;
import com.svnlike.api.model.dto.repository.response.SvnFileDto;
import com.svnlike.api.model.po.RepositoryPO;
import com.svnlike.svnapi.model.SvnEntry;
import com.svnlike.utils.exception.SvnLikeException;

import javax.validation.Valid;
import java.util.List;

/**
 * Repository Service
 *
 * @author zhangkx
 */
public interface IRepositoryService {

    /**
     * Get repository list
     *
     * @param input parameters
     * @return List
     */
    List<RepositoryPO> getRepositoryList(RepositoryConditionDTO input);

    /**
     * Get repository list by project id
     *
     * @param projectId project id
     * @return List
     */
    List<RepositoryPO> getRepositoryListByProjectId(Integer projectId);

    /**
     * Get repository by id
     *
     * @param id repositoryId
     * @return Repository
     */
    RepositoryPO getRepositoryById(int id);

    /**
     * Get repository by url name
     *
     * @param projectUrlName    project url name
     * @param repositoryUrlName repository url name
     * @return Repository
     */
    RepositoryPO getRepositoryByUrlName(String projectUrlName, String repositoryUrlName);

    /**
     * Create repository
     *
     * @param repositoryName repository name
     * @return repository id
     * @throws SvnLikeException exception
     */
    void createRepository(RepositoryInputDTO repositoryName);

    /**
     * Update repository
     *
     * @param id    repositoryId
     * @param input parameters
     */
    void updateRepositoryById(int id, @Valid RepositoryInputDTO input);

    /**
     * Delete repository
     *
     * @param id repositoryId
     */
    void deleteRepositoryById(int id);

//    /**
//     * Get repository tree
//     *
//     * @param id     repositoryId
//     * @param path   repository path
//     * @param getALl get all children
//     * @return SVNTreeItem
//     */
//    SVNTreeItem getRepositoryTreeById(int id, String path, Boolean getALl);

    /**
     * get repository tree by name
     *
     * @param repositoryId repository id
     * @param path         repository path
     * @return SVNTreeItem
     */
    List<SvnEntry> getRepositoryTree(Integer repositoryId, String path);

    /**
     * get file message
     *
     * @param repositoryId repository id
     * @param path         path
     * @return string file message
     */
    SvnFileDto getRepositoryFile(Integer repositoryId, String path);
}
