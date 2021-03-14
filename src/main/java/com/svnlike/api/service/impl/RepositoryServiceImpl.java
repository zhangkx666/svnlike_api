package com.svnlike.api.service.impl;

import com.svnlike.api.model.dto.repository.request.RepositoryConditionDTO;
import com.svnlike.api.model.dto.repository.request.RepositoryInputDTO;
import com.svnlike.api.model.entity.Repository;
import com.svnlike.api.model.entity.SVNFile;
import com.svnlike.api.model.entity.SVNTreeItem;
import com.svnlike.api.model.po.RepositoryPO;
import com.svnlike.api.service.IProjectService;
import com.svnlike.api.service.IRepositoryService;
import com.svnlike.svnapi.ISvnAdmin;
import com.svnlike.svnapi.ISvnClient;
import com.svnlike.svnapi.model.SvnRepository;
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

import static com.svnlike.svnapi.enums.ESvnProtocol.SVN;

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

    private final ISvnAdmin svnAdmin;

    private final ISvnClient svnClient;

    private final IProjectService projectService;

    @Autowired
    public RepositoryServiceImpl(ISvnAdmin svnAdmin, ISvnClient svnClient, IProjectService projectService) {
        this.svnAdmin = svnAdmin;
        this.svnClient = svnClient;
        this.projectService = projectService;
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
     * Get repository list by project id
     *
     * @param projectId project id
     * @return List
     */
    @Override
    public List<RepositoryPO> getRepositoryListByProjectId(Integer projectId) {

        // Query parameters
        HashMap<String, Object> params = new HashMap<>(1);
        params.put("projectId", projectId);

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
     * Get repository by url name
     *
     * @param projectUrlName project url name
     * @param repositoryUrlName repository url name
     * @return Repository
     */
    @Override
    @Cacheable(value = "repository", key = "'name=' + #projectUrlName + '/' +  #repositoryUrlName")
    public RepositoryPO getRepositoryByUrlName(String projectUrlName, String repositoryUrlName) {
        // Query parameters
        HashMap<String, Object> params = new HashMap<>(2);
        params.put("projectUrlName", projectUrlName);
        params.put("repositoryUrlName", repositoryUrlName);

        // Query repository list
        RepositoryPO repositoryPO = commonDAO.queryForObject("Repository.selectOneByUrlName", params, RepositoryPO.class);
        if (repositoryPO == null) {
            throw new NotFoundException(m.error("repository.name_not_exists", repositoryUrlName));
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

        // 1. check project url name
        if (!projectService.projectUrlNameExists(input.getProjectUrlName())) {
            throw new SvnLikeException(m.error("project.urlName.notfound", input.getProjectUrlName()));
        }

        // 2. repository exists check
        if (repositoryNameExists(input.getName())) {
            throw new SvnLikeException(m.error("repository.name.duplicate.create", input.getName()));
        }

        // 3. check if url name is exists
        if (repositoryUrlNameExists(input.getUrlName())) {
            throw new SvnLikeException(m.error("repository.urlName.duplicate.create", input.getUrlName()));
        }

        // 4. create repository
        SvnRepository svnRepository = new SvnRepository();
        svnRepository.setProjectUrlName(input.getProjectUrlName());
        svnRepository.setName(input.getUrlName());
        svnRepository = svnAdmin.createRepository(svnRepository);

        if (input.getInitWithTrunk()) {
            this.svnClient.setRootPath("file:///C:/Users/zhangkx/svn/" + input.getProjectUrlName() + "/" + input.getUrlName());
            this.svnClient.setSvnUser(svnRepository.getAdminUser());

            // trunk
            svnClient.mkdir("trunk", "init");

            // create branches
            svnClient.mkdir("branches", "init");

            // create tags
            svnClient.mkdir("tags", "init");
        }

        // 5. write repository db data
        Repository repository = new Repository();

        // name, urlName, projectId, description, visibility
        repository.copyPropertiesFrom(input);
        repository.setUserId(uvo.getId());
        repository.setLocalPath(svnRepository.getRepoRootPath());
        repository.setProtocol(SVN);
        commonDAO.execute("Repository.add", repository);
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

            File oldFolder = new File(repository.getLocalPath());
            File newFolder = new File(oldFolder.getParentFile().getPath() + "/" + newRepositoryName);
            try {
                FileUtils.moveDirectory(oldFolder, newFolder);
            } catch (IOException e) {
                logger.error(e.getMessage());
            }

            // reset name and path
            repository.setName(newRepositoryName);
            repository.setLocalPath(StringUtils.windowsPath2LinuxPath(newFolder.getPath()));
        }

        // update title and description
        repository.setUrlName(input.getUrlName());
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
            FileUtils.deleteDirectory(new File(repositoryPO.getLocalPath()));
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
     * @param getAll get all children
     * @return SVNTreeItem
     */
    @Override
    @Cacheable(value = "repository.tree", key = "'id=' + #id + ',path=' + #path + ',getAll=' + #getALl")
    public SVNTreeItem getRepositoryTreeById(int id, String path, Boolean getAll) {

        // query repository
        RepositoryPO repositoryPO = this.getRepositoryById(id);
        if (repositoryPO == null) {
            throw new SvnLikeException(m.error("repository.not_exists", String.valueOf(id)));
        }

        // get repository tree
        String repositoryPath = repositoryPO.getProtocol().getPrefix() + repositoryPO.getLocalPath();

        return null;
    }

    /**
     * get repository tree by name
     *
     * @param repositoryName repository name
     * @param path           repository path
     * @param getAll         get all children
     * @return SVNTreeItem
     */
    @Override
    @Cacheable(value = "repository.tree", key = "'name=' + #repositoryName + ',path=' + #path + ',getAll=' + #getAll")
    public SVNTreeItem getRepositoryTreeByName(String repositoryName, String path, Boolean getAll) {

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
        RepositoryPO repositoryPO = this.getRepositoryByUrlName(repositoryName, repositoryName);
        if (repositoryPO == null) {
            throw new SvnLikeException(m.error("repository.name_not_exists", repositoryName));
        }

        // get repository tree
        String repositoryPath = repositoryPO.getProtocol().getPrefix() + repositoryPO.getLocalPath();

//        return repositoryBaseService.getRepositoryFile(repositoryPath, path);

        return null;
    }

    /**
     * check if the repository name exists
     *
     * @param name repository name
     * @return boolean
     */
    private boolean repositoryNameExists(String name) {
        HashMap<String, Object> params = new HashMap<>(1);
        params.put("name", name);
        return commonDAO.checkExists("Repository.selectCountByName", params);
    }

    /**
     * check if the repository url name exists
     *
     * @param urlName repository name
     * @return boolean
     */
    private boolean repositoryUrlNameExists(String urlName) {
        HashMap<String, Object> params = new HashMap<>(1);
        params.put("urlName", urlName);
        return commonDAO.checkExists("Repository.selectCountByUrlName", params);
    }
}
