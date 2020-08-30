package com.svnlike.api.service.impl;

import com.marssvn.svnapi.ISvnAdmin;
import com.marssvn.svnapi.ISvnClient;
import com.marssvn.svnapi.model.SvnRepository;
import com.svnlike.api.model.dto.repository.request.RepositoryConditionDTO;
import com.svnlike.api.model.dto.repository.request.RepositoryInputDTO;
import com.svnlike.api.model.entity.Repository;
import com.svnlike.api.model.entity.SVNFile;
import com.svnlike.api.model.entity.SVNTreeItem;
import com.svnlike.api.model.po.RepositoryPO;
import com.svnlike.api.service.IRepositoryService;
import com.svnlike.utils.annotation.cache.CacheRemove;
import com.svnlike.utils.common.StringUtils;
import com.svnlike.utils.exception.NotFoundException;
import com.svnlike.utils.exception.SvnLikeException;
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

import static com.svnlike.utils.enums.ESvnProtocol.SVN;

/**
 * Repository Service
 *
 * @author zhangkx
 */
@Service
public class RepositoryServiceImpl extends BaseService implements IRepositoryService {

    /**
     * repository init comment
     */
    private static final String INIT_COMMENT = "init";

    private ISvnAdmin svnAdmin;

    private final ISvnClient svnClient;

    @Autowired
    public RepositoryServiceImpl(ISvnAdmin svnAdmin, ISvnClient svnClient) {
        this.svnAdmin = svnAdmin;
        this.svnClient = svnClient;
    }

    /**
     * Get repository list
     *
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
     *
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
        RepositoryPO repositoryPO = commonDAO.queryForObject("Repository.selectOneByName", params, RepositoryPO.class);
        if (repositoryPO == null) {
            throw new NotFoundException(m.error("repository.name_not_exists", name));
        }
        return repositoryPO;
    }

    /**
     * create repository
     *
     * @param input RepositoryInputDTO
     * @return SVNURL
     * @throws SvnLikeException exception
     */
    @Override
    @Transactional
    @CacheEvict(value = "repository.list", allEntries = true)
    public void createRepository(RepositoryInputDTO input) {
        String repositoryName = input.getName();

        // 1. repository exists check
        if (repositoryNameExists(repositoryName)) {
            throw new SvnLikeException(m.error("repository.name.duplicate.create", repositoryName));
        }

        // 2. create repository
        SvnRepository svnRepository = new SvnRepository();
        svnRepository.setName(repositoryName);
        svnRepository = svnAdmin.createRepository(svnRepository);

        // 3. write repository db data
        Repository repository = new Repository();
        repository.setUserId(uvo.getId());
        repository.setName(repositoryName);
        repository.setTitle(input.getUrlName());
        repository.setProjectId(input.getProjectId());
        repository.setDescription(input.getDescription());
        repository.setPath(svnRepository.getFullPathLocal());
        repository.setProtocol(SVN);

//        try {
//            commonDAO.execute("Repository.add", repository);
//
//            // 4. create trunk, branches, tags
//            // if autoMakeDir = true, create trunk, branches, tags directory
//            if (input.getAutoMakeDir()) {
//                this.svnClient.setRootPath(repository.getUrl());
//                this.svnClient.setSvnUser(new SVNUser("", ""));
//
//                logger.info("-------------");
//                logger.info(svnPath);
//                logger.info(repository.getUrl());
//
//                // trunk
//                svnClient.mkdir("trunk", "init");
//
//                // create branches
//                svnClient.mkdir("branches", "init");
//
//                // create tags
//                svnClient.mkdir("tags", "init");
//            }
//        } catch (Exception e) {
//
//            // 4.1 delete repository folder and throw business exception when insert failed
//            try {
//                FileUtils.deleteDirectory(new File(repository.getPath()));
//            } catch (IOException ex) {
//                logger.error(ex.getMessage());
//            }
//            e.printStackTrace();
//            throw new SvnLikeException(m.error("repository.create.failed"));
//        }
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
            throw new SvnLikeException(m.error("repository.not_exists", String.valueOf(id)));
        }

        // Repository
        Repository repository = repositoryPO.convertTo(Repository.class);

        // if new repository name not equals old name
        if (StringUtils.isNotBlank(newRepositoryName) && !newRepositoryName.equals(repository.getName())) {

            // new repository name
            if (repositoryNameExists(newRepositoryName)) {
                throw new SvnLikeException(m.error("repository.name.duplicate.update", newRepositoryName));
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
        repository.setTitle(input.getUrlName());
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
            throw new SvnLikeException(m.error("repository.not_exists", String.valueOf(id)));
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
            throw new SvnLikeException(m.error("repository.delete.failed"));
        }
    }

    /**
     * Get repository tree
     *
     * @param id     repositoryId
     * @param path   repository path
     * @param getALl get all children
     * @return SVNTreeItem
     */
    @Override
    @Cacheable(value = "repository.tree", key = "'id=' + #id + ',path=' + #path + ',getAll=' + #getALl")
    public SVNTreeItem getRepositoryTreeById(int id, String path, Boolean getALl) {

        // query repository
        RepositoryPO repositoryPO = this.getRepositoryById(id);
        if (repositoryPO == null) {
            throw new SvnLikeException(m.error("repository.not_exists", String.valueOf(id)));
        }

        // get repository tree
        String repositoryPath = repositoryPO.getProtocol().getPrefix() + repositoryPO.getPath();

        return null;
    }

    /**
     * get repository tree by name
     *
     * @param repositoryName repository name
     * @param path           repository path
     * @param getALl         get all children
     * @return SVNTreeItem
     */
    @Override
    @Cacheable(value = "repository.tree", key = "'name=' + #repositoryName + ',path=' + #path + ',getAll=' + #getALl")
    public SVNTreeItem getRepositoryTreeByName(String repositoryName, String path, Boolean getALl) {

//        // query repository
//        RepositoryPO repositoryPO = this.getRepositoryByName(repositoryName);
//        if (repositoryPO == null) {
//            throw new SvnLikeException(m.error("repository.name_not_exists", repositoryName));
//        }
//
//        // get repository tree
//        String repositoryPath = repositoryPO.getProtocol().getPrefix() + repositoryPO.getPath();
//
//        return this.getRepositoryTree(repositoryPath, repositoryPO.getName(), path, getALl);

        return null;
    }

    /**
     * get file message
     *
     * @param repositoryName repository name
     * @param path           path
     * @return string file message
     */
    @Override
    @Cacheable(value = "repository.file", key = "'name=' + #repositoryName + ',path=' + #path")
    public SVNFile getRepositoryFile(String repositoryName, String path) {
        // query repository
        RepositoryPO repositoryPO = this.getRepositoryByName(repositoryName);
        if (repositoryPO == null) {
            throw new SvnLikeException(m.error("repository.name_not_exists", repositoryName));
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
    private boolean repositoryNameExists(String repositoryName) {
        HashMap<String, Object> params = new HashMap<>(1);
        params.put("name", repositoryName);
        return commonDAO.checkExists("Repository.selectCountByName", params);
    }
}
