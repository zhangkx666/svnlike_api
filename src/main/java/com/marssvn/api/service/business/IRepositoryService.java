package com.marssvn.api.service.business;

import com.marssvn.api.model.dto.repository.request.RepositoryConditionDTO;
import com.marssvn.api.model.dto.repository.request.RepositoryInputDTO;
import com.marssvn.api.model.entity.SVNFile;
import com.marssvn.api.model.entity.SVNTreeItem;
import com.marssvn.api.model.po.RepositoryPO;
import com.marssvn.utils.exception.BusinessException;

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
     * Get repository by id
     *
     * @param id repositoryId
     * @return Repository
     */
    RepositoryPO getRepositoryById(int id);

    /**
     * Get repository by name
     *
     * @param name repository name
     * @return Repository
     */
    RepositoryPO getRepositoryByName(String name);

    /**
     * Create repository
     *
     * @param repositoryName repository name
     * @return repository id
     * @throws BusinessException exception
     */
    int createRepository(RepositoryInputDTO repositoryName);

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

    /**
     * Get repository tree
     *
     * @param id     repositoryId
     * @param path   repository path
     * @param getALl get all children
     * @return SVNTreeItem
     */
    SVNTreeItem getRepositoryTreeById(int id, String path, Boolean getALl);

    /**
     * get repository tree by name
     * @param repositoryName           repository name
     * @param path           repository path
     * @param getALl         get all children
     * @return SVNTreeItem
     */
    SVNTreeItem getRepositoryTreeByName(String repositoryName, String path, Boolean getALl);

    /**
     * get file content
     * @param repositoryName repository name
     * @param path path
     * @return string file content
     */
    SVNFile getRepositoryFile(String repositoryName, String path);
}
