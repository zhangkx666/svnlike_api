package com.marssvn.api.service.business;

import com.marssvn.api.model.dto.repository.request.RepositoryConditionDTO;
import com.marssvn.api.model.dto.repository.request.RepositoryInputDTO;
import com.marssvn.api.model.dto.repository.response.RepositoryTreeDTO;
import com.marssvn.api.model.po.RepositoryPO;
import com.marssvn.utils.exception.BusinessException;

import javax.validation.Valid;
import java.util.List;

/**
 * Repository Service
 * @author zhangkx
 */
public interface IRepositoryService {

    /**
     * Get repository list
     * @param input parameters
     * @return List
     */
    List<RepositoryPO> getRepositoryList(RepositoryConditionDTO input);

    /**
     * Get repository by id
     * @param id repositoryId
     * @return Repository
     */
    RepositoryPO getRepositoryById(int id);

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
     * @param id repositoryId
     * @param input parameters
     */
    void updateRepositoryById(int id, @Valid RepositoryInputDTO input);

    /**
     * Delete repository
     * @param id repositoryId
     */
    void deleteRepositoryById(int id);

    /**
     * Get repository tree
     * @param id repositoryId
     * @param path repository path
     * @return
     */
    RepositoryTreeDTO getRepositoryTreeById(int id, String path);
}
