package com.marssvn.api.service.business.impl;

import com.marssvn.api.model.dto.repository.request.RepositoryConditionDTO;
import com.marssvn.api.model.dto.repository.request.RepositoryInputDTO;
import com.marssvn.api.model.dto.repository.response.RepositoryTreeDTO;
import com.marssvn.api.model.entity.Repository;
import com.marssvn.api.service.base.RepositoryBaseService;
import com.marssvn.api.service.business.IRepositoryService;
import com.marssvn.utils.common.StringUtils;
import com.marssvn.utils.exception.BusinessException;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNURL;

import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import static com.marssvn.utils.enums.ESvnProtocol.FILE;

/**
 * Repository Service
 */
@Service
public class RepositoryService extends BaseService implements IRepositoryService {

    /**
     * repository init comment
     */
    private static final String INIT_COMMENT = "init";

    @Autowired
    private RepositoryBaseService repositoryBaseService;

    @Override
    public List<Repository> getRepositoryList(RepositoryConditionDTO input) {

        // Query parameters
        HashMap<String, Object> params = new HashMap<>();
        params.put("keyword", input.getKeyword());
        params.put("offset", (input.getPage() - 1) * input.getPageSize());
        params.put("pageSize", input.getPageSize());

        // Query repository list
        return commonDAO.queryForList("Repository.selectMany", params);
    }

    @Override
    public Repository getRepositoryById(int id) {

        // Query parameters
        HashMap<String, Object> params = new HashMap<>();
        params.put("id", id);

        // Query repository list
        return commonDAO.queryForObject("Repository.selectOne", params, Repository.class);
    }

    /**
     * create repository
     *
     * @param input RepositoryInputDTO
     * @return SVNURL
     * @throws BusinessException exception
     */
    @Override
    @Transactional
    public int createRepository(RepositoryInputDTO input) throws BusinessException {
        String repositoryName = input.getName();

        // 1. repository exists check
        if (_repositoryNameExists(repositoryName))
            throw new BusinessException(message.error("repository.name.duplicate.create", repositoryName));

        // 2. create repository
        SVNURL svnurl = repositoryBaseService.createSvnRepository(repositoryName);

        // 3. create trunk, branches, tags
        // if autoMakeDir = true, create trunk, branches, tags directory
        if (input.getAutoMakeDir()) {
            try {
                // trunk
                SVNURL trunk = svnurl.appendPath("trunk", false);

                // create branches
                SVNURL branches = svnurl.appendPath("branches", false);

                // create tags
                SVNURL tags = svnurl.appendPath("tags", false);

                repositoryBaseService.createDirectory(new SVNURL[]{trunk, branches, tags}, INIT_COMMENT);
            } catch (SVNException | BusinessException e) {
                logger.error(e.getMessage());
            }
        }

        // 4. write repository db data
        Repository repository = new Repository();
        repository.setUserId(uvo.getId());
        repository.setName(repositoryName);
        repository.setProjectId(input.getProjectId());
        repository.setDescription(input.getDescription());
        repository.setPath(svnurl.getPath());
        repository.setProtocol(FILE);

        try {
            commonDAO.execute("Repository.add", repository);
        } catch (Exception e) {

            // 3.1 delete repository folder and throw business exception when insert failed
            try {
                FileUtils.deleteDirectory(new File(repository.getPath()));
            } catch (IOException ex) {
                logger.error(ex.getMessage());
            }
            logger.error(e.getMessage());
            throw new BusinessException(message.error("repository.create.failed"));
        }

        return repository.getId();
    }

    /**
     * Update repository
     *
     * @param id    repositoryId
     * @param input parameters
     */
    @Override
    @Transactional
    public void updateRepositoryById(int id, @Valid RepositoryInputDTO input) {
        String newRepositoryName = input.getName();

        // query repository
        Repository repository = this.getRepositoryById(id);
        if (repository == null)
            throw new BusinessException(message.error("repository.not_exists", String.valueOf(id)));

        // if new repository name not equals old name
        if (StringUtils.isNotBlank(newRepositoryName) && !newRepositoryName.equals(repository.getName())) {

            // new repository name
            if (_repositoryNameExists(newRepositoryName))
                throw new BusinessException(message.error("repository.name.duplicate.update", newRepositoryName));

            File oldFolder = new File(repository.getPath());
            File newFolder = new File(oldFolder.getParentFile().getPath() + "/" + newRepositoryName);
            try {
                FileUtils.moveDirectory(oldFolder, newFolder);
            } catch (IOException e) {
                logger.error(e.getMessage());
            }

            // reset name and path
            repository.setName(newRepositoryName);
            repository.setPath(StringUtils.windowsPath2LinuxPath(newFolder.getPath()));
        }

        repository.setDescription(input.getDescription());

        // update db
        commonDAO.execute("Repository.updateById", repository);
    }

    /**
     * Delete repository
     *
     * @param id repositoryId
     */
    @Override
    @Transactional
    public void deleteRepositoryById(int id) {

        // query repository
        Repository repository = this.getRepositoryById(id);
        if (repository == null)
            throw new BusinessException(message.error("repository.not_exists", String.valueOf(id)));

        // delete db data
        HashMap<String, Object> params = new HashMap<>();
        params.put("id", id);
        commonDAO.execute("Repository.deleteById", params);

        // delete svn folder
        try {
            FileUtils.deleteDirectory(new File(repository.getPath()));
        } catch (IOException e) {
            logger.error(e.getMessage());
            throw new BusinessException(message.error("repository.delete.failed"));
        }
    }

    /**
     * get repository tree
     *
     * @param id   repository id
     * @param path repository path
     * @return repository tree
     */
    @Override
    public RepositoryTreeDTO getRepositoryTreeById(int id, String path) {

        // query repository
        Repository repository = this.getRepositoryById(id);
        if (repository == null)
            throw new BusinessException(message.error("repository.not_exists", String.valueOf(id)));

        // get repository tree
        String repositoryPath = repository.getProtocol().getPrefix() + repository.getPath();
        return repositoryBaseService.getRepositoryTree(repositoryPath, repository.getName(), path);
    }

    /**
     * Verify if the repository name exists
     *
     * @param repositoryName repository name
     * @return boolean
     */
    private boolean _repositoryNameExists(String repositoryName) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("name", repositoryName);
        int count = commonDAO.queryForObject("Repository.selectCountByName", params, Integer.class);
        return count > 0;
    }
}
