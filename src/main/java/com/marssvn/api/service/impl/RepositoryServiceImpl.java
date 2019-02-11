package com.marssvn.api.service.impl;

import com.marssvn.api.model.dto.repository.request.RepositoryConditionDTO;
import com.marssvn.api.model.dto.repository.request.RepositoryInputDTO;
import com.marssvn.api.model.entity.Repository;
import com.marssvn.api.model.entity.SVNFile;
import com.marssvn.api.model.entity.SVNTreeItem;
import com.marssvn.api.model.po.RepositoryPO;
import com.marssvn.api.service.IRepositoryService;
import com.marssvn.svnapi.ISVNAdmin;
import com.marssvn.svnapi.ISVNClient;
import com.marssvn.svnapi.model.SVNUser;
import com.marssvn.utils.annotation.cache.CacheRemove;
import com.marssvn.utils.common.StringUtils;
import com.marssvn.utils.exception.BusinessException;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import static com.marssvn.utils.enums.ESVNProtocol.SVN;

/**
 * Repository Service
 * @author zhangkx
 */
@Service
public class RepositoryServiceImpl extends BaseService implements IRepositoryService {

    /**
     * repository init comment
     */
    private static final String INIT_COMMENT = "init";

    private ISVNAdmin svnAdmin;

    private final ISVNClient svnClient;

    @Autowired
    public RepositoryServiceImpl(ISVNAdmin svnAdmin, ISVNClient svnClient) {
        this.svnAdmin = svnAdmin;
        this.svnClient = svnClient;
    }

    /**
     * Get repository list
     * @param input parameters
     * @return List
     */
    @Override
    @Cacheable("repository.list")
    public List<RepositoryPO> getRepositoryList(RepositoryConditionDTO input) {

        // Query parameters
        HashMap<String, Object> params = new HashMap<>(3);
        params.put("keyword", input.getKeyword());
        params.put("offset", (input.getPage() - 1) * input.getPageSize());
        params.put("pageSize", input.getPageSize());

        // Query repository list
        return commonDAO.queryForList("Repository.selectMany", params);
    }

    /**
     * Get repository by id
     * @param id repositoryId
     * @return Repository
     */
    @Override
    @Cacheable(value = "repository", key = "'id=' + #id")
    public RepositoryPO getRepositoryById(int id) {

        // Query parameters
        HashMap<String, Object> params = new HashMap<>(1);
        params.put("id", id);

        // Query repository list
        return commonDAO.queryForObject("Repository.selectOne", params, RepositoryPO.class);
    }

    /**
     * Get repository by name
     *
     * @param name repository name
     * @return Repository
     */
    @Override
    @Cacheable(value = "repository", key = "'name=' + #name")
    public RepositoryPO getRepositoryByName(String name) {
        // Query parameters
        HashMap<String, Object> params = new HashMap<>(1);
        params.put("name", name);

        // Query repository list
        return commonDAO.queryForObject("Repository.selectOneByName", params, RepositoryPO.class);
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
    @CacheEvict(value = "repository.list", allEntries = true)
    public int createRepository(RepositoryInputDTO input) {
        String repositoryName = input.getName();

        // 0. repository name can't be blank
        if (StringUtils.isBlank(repositoryName)) {
            throw new BusinessException(message.error("repository.name.blank"));
        }

        // 1. repository exists check
        if (_repositoryNameExists(repositoryName)) {
            throw new BusinessException(message.error("repository.name.duplicate.create", repositoryName));
        }

        // 2. create repository
        String svnPath = svnAdmin.createRepository(null, repositoryName);

        // 3. write repository db data
        Repository repository = new Repository();
        repository.setUserId(uvo.getId());
        repository.setName(repositoryName);
        repository.setTitle(input.getTitle());
        repository.setProjectId(input.getProjectId());
        repository.setDescription(input.getDescription());
        repository.setPath(svnPath);
        repository.setProtocol(SVN);

        try {
            commonDAO.execute("Repository.add", repository);

            // 4. create trunk, branches, tags
            // if autoMakeDir = true, create trunk, branches, tags directory
            if (input.getAutoMakeDir()) {

                this.svnClient.setRootPath(repository.getUrl());
                this.svnClient.setSvnUser(new SVNUser("", ""));

                // trunk
                svnClient.mkdir("trunk", "init");

                // create branches
                svnClient.mkdir("branches", "init");

                // create tags
                svnClient.mkdir("tags", "init");
            }
        } catch (Exception e) {

            // 4.1 delete repository folder and throw business exception when insert failed
            try {
                FileUtils.deleteDirectory(new File(repository.getPath()));
            } catch (IOException ex) {
                logger.error(ex.getMessage());
            }
            e.printStackTrace();
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
    @CacheRemove({"repository.list::*", "'repository::id=' + #id", "'repository.tree::id=' + #id + '*'"})
    public void updateRepositoryById(int id, @Valid RepositoryInputDTO input) {
        String newRepositoryName = input.getName();

        // query repository
        RepositoryPO repositoryPO = this.getRepositoryById(id);
        if (repositoryPO == null) {
            throw new BusinessException(message.error("repository.not_exists", String.valueOf(id)));
        }

        // Repository
        Repository repository = repositoryPO.convertTo(Repository.class);

        // if new repository name not equals old name
        if (StringUtils.isNotBlank(newRepositoryName) && !newRepositoryName.equals(repository.getName())) {

            // new repository name
            if (_repositoryNameExists(newRepositoryName)) {
                throw new BusinessException(message.error("repository.name.duplicate.update", newRepositoryName));
            }

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

        // update title and description
        repository.setTitle(input.getTitle());
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
    @CacheRemove({"repository.list::*", "'repository::id=' + #id", "'repository.tree::id=' + #id + '*'"})
    public void deleteRepositoryById(int id) {

        // query repository
        RepositoryPO repositoryPO = this.getRepositoryById(id);
        if (repositoryPO == null) {
            throw new BusinessException(message.error("repository.not_exists", String.valueOf(id)));
        }

        // delete db data
        HashMap<String, Object> params = new HashMap<>(1);
        params.put("id", id);
        commonDAO.execute("Repository.deleteById", params);

        // delete svn folder
        try {
            FileUtils.deleteDirectory(new File(repositoryPO.getPath()));
        } catch (IOException e) {
            logger.error(e.getMessage());
            throw new BusinessException(message.error("repository.delete.failed"));
        }
    }

//    /**
//     * Get repository tree
//     *
//     * @param id     repositoryId
//     * @param path   repository path
//     * @param getALl get all children
//     * @return SVNTreeItem
//     */
//    @Override
//    @Cacheable(value = "repository.tree", key = "'id=' + #id + ',path=' + #path + ',getAll=' + #getALl")
//    public SVNTreeItem getRepositoryTreeById(int id, String path, Boolean getALl) {
//
//        // query repository
//        RepositoryPO repositoryPO = repositoryService.getRepositoryById(id);
//        if (repositoryPO == null) {
//            throw new BusinessException(message.error("repository.not_exists", String.valueOf(id)));
//        }
//
//        // get repository tree
//        String repositoryPath = repositoryPO.getProtocol().getPrefix() + repositoryPO.getPath();
//
//        return repositoryBaseService.getRepositoryTree(repositoryPath, repositoryPO.getName(), path, getALl);
//    }

    /**
     * get repository tree by name
     * @param repositoryName           repository name
     * @param path           repository path
     * @param getALl         get all children
     * @return SVNTreeItem
     */
    @Override
    @Cacheable(value = "repository.tree", key = "'name=' + #repositoryName + ',path=' + #path + ',getAll=' + #getALl")
    public SVNTreeItem getRepositoryTreeByName(String repositoryName, String path, Boolean getALl) {

//        // query repository
//        RepositoryPO repositoryPO = repositoryService.getRepositoryByName(repositoryName);
//        if (repositoryPO == null) {
//            throw new BusinessException(message.error("repository.name_not_exists", repositoryName));
//        }
//
//        // get repository tree
//        String repositoryPath = repositoryPO.getProtocol().getPrefix() + repositoryPO.getPath();
//
//        return repositoryBaseService.getRepositoryTree(repositoryPath, repositoryPO.getName(), path, getALl);

        return null;
    }

    /**
     * get file content
     * @param repositoryName repository name
     * @param path path
     * @return string file content
     */
    @Override
    @Cacheable(value = "repository.file",  key = "'name=' + #repositoryName + ',path=' + #path")
    public SVNFile getRepositoryFile(String repositoryName, String path) {
        // query repository
        RepositoryPO repositoryPO = this.getRepositoryByName(repositoryName);
        if (repositoryPO == null) {
            throw new BusinessException(message.error("repository.name_not_exists", repositoryName));
        }

        // get repository tree
        String repositoryPath = repositoryPO.getProtocol().getPrefix() + repositoryPO.getPath();

//        return repositoryBaseService.getRepositoryFile(repositoryPath, path);

        return null;
    }

    /**
     * Verify if the repository name exists
     *
     * @param repositoryName repository name
     * @return boolean
     */
    private boolean _repositoryNameExists(String repositoryName) {
        HashMap<String, Object> params = new HashMap<>(1);
        params.put("name", repositoryName);
        int count = commonDAO.queryForObject("Repository.selectCountByName", params, Integer.class);
        return count > 0;
    }
}
