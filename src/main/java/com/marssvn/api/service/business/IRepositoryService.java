package com.marssvn.api.service.business;

import com.marssvn.api.model.dto.repository.RepositoryConditionDTO;
import com.marssvn.api.model.dto.repository.RepositoryInputDTO;
import com.marssvn.api.model.dto.repository.RepositoryTreeDTO;
import com.marssvn.api.model.entity.Repository;
import com.marssvn.utils.exception.BusinessException;

import javax.validation.Valid;
import java.util.List;

/**
 * Repository Service
 */
public interface IRepositoryService {

    /**
     * Get repository list
     * @param input parameters
     * @return List
     */
    List<Object> getRepositoryList(RepositoryConditionDTO input);

    /**
     * Get repository by id
     * @param id repositoryId
     * @return Repository
     */
    Repository getRepositoryById(int id);

    /**
     * Create repository
     *
     * @param repositoryName repository name
     * @return repository id
     * @throws BusinessException exception
     */
    int createRepository(RepositoryInputDTO repositoryName) throws BusinessException;

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

    RepositoryTreeDTO getRepositoryTreeById(int id, String path);
}
