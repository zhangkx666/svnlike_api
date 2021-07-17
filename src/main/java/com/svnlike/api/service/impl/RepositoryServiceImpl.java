package com.svnlike.api.service.impl;

import com.svnlike.api.model.dto.repository.request.RepositoryConditionDTO;
import com.svnlike.api.model.dto.repository.request.RepositoryInputDTO;
import com.svnlike.api.model.entity.Repository;
import com.svnlike.api.model.dto.repository.response.SvnFileDto;
import com.svnlike.api.model.po.RepositoryPO;
import com.svnlike.api.service.IProjectService;
import com.svnlike.api.service.IRepositoryService;
import com.svnlike.svnapi.ISvnAdmin;
import com.svnlike.svnapi.ISvnClient;
import com.svnlike.svnapi.model.SvnEntry;
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
     * @param projectUrlName    project url name
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
        if (repositoryNameExists(input.getProjectId(), input.getName())) {
            throw new SvnLikeException(m.error("repository.name.duplicate.create", input.getName()));
        }

        // 3. check if url name is exists
        if (repositoryUrlNameExists(input.getProjectId(), input.getUrlName())) {
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
        repository.setLocalPath(StringUtils.windowsPath2LinuxPath(svnRepository.getRepoRootPath()));
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
            if (repositoryNameExists(input.getProjectId(), newRepositoryName)) {
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
     * get repository tree
     *
     * @param repositoryId repository id
     * @param path         repository path
     * @return SVNTreeItem
     */
    @Override
    @Cacheable(value = "repository.tree", key = "'id=' + #repositoryId + ',path=' + #path")
    public List<SvnEntry> getRepositoryTree(Integer repositoryId, String path) {

        // query repository
        RepositoryPO repository = this.getRepositoryById(repositoryId);
        if (repository == null) {
            throw new SvnLikeException(m.error("repository.not_exists", String.valueOf(repositoryId)));
        }

        // get repository tree
        String repositoryPath = repository.getProtocol().getPrefix() + repository.getLocalPath();
        if (StringUtils.isNotBlank(repository.getLocalPath())) {
            this.svnClient.setRootPath("file://" + repository.getLocalPath());
        } else if (StringUtils.isNotBlank(repository.getSvnUrl())) {
            this.svnClient.setRootPath(repository.getSvnUrl());
        } else {
            throw new SvnLikeException(m.error("repository.unknown_root_path"));
        }

        return this.svnClient.list(path);
    }

    /**
     * get file message
     *
     * @param repositoryId repository id
     * @param path         path
     * @return string file message
     */
    @Override
    @Cacheable(value = "repository.file", key = "'id=' + #repositoryId + ',path=' + #path")
    public SvnFileDto getRepositoryFile(Integer repositoryId, String path) {
        // query repository
        RepositoryPO repository = this.getRepositoryById(repositoryId);
        if (repository == null) {
            throw new SvnLikeException(m.error("repository.not_exists", String.valueOf(repositoryId)));
        }

        if (StringUtils.isNotBlank(repository.getLocalPath())) {
            this.svnClient.setRootPath("file://" + repository.getLocalPath());
        } else if (StringUtils.isNotBlank(repository.getSvnUrl())) {
            this.svnClient.setRootPath(repository.getSvnUrl());
        } else {
            throw new SvnLikeException(m.error("repository.unknown_root_path"));
        }

        SvnFileDto svnFileDto = new SvnFileDto();
        svnFileDto.setName("readme.md");
        svnFileDto.setContent("> markdown是一种可以使用普通文本编辑器编写的标记语言，通过简单的标记语法，它可以使普通文本内容具有一定的格式。\n" +
                "\n" +
                "### 标题\n" +
                "---\n" +
                "\n" +
                "# 一级标题\n" +
                "## 二级标题\n" +
                "### 三级标题\n" +
                "#### 四级标题\n" +
                "##### 五级标题\n" +
                "###### 六级标题\n" +
                "\n" +
                "```lang-markdown\n" +
                "# 一级标题\n" +
                "## 二级标题\n" +
                "### 三级标题\n" +
                "#### 四级标题\n" +
                "##### 五级标题\n" +
                "###### 六级标题\n" +
                "```\n" +
                "\n" +
                "### 样式 [^批注：样式]\n" +
                "**粗体**  *斜体* ***粗斜体*** ~~删除线~~ <sup>上标</sup> <sub>下标</sub>  <b class='red'>红色</b> <b class='blue'>蓝色</b> <b " +
                "class='green'>绿色</b> \n" +
                "\n" +
                "```lang-markdown\n" +
                "**粗体**  *斜体* ***粗斜体*** ~~删除线~~ <sup>上标</sup> <sub>下标</sub>  <b class='red'>红色</b> <b class='blue'>蓝色</b> <b " +
                "class='green'>绿色</b> \n" +
                "```\n" +
                "\n" +
                "### 分割线 \n" +
                "***\n" +
                "***\n" +
                "\n" +
                "```lang-markdown\n" +
                "---\n" +
                "***\n" +
                "```\n" +
                "\n" +
                "### 引用内容 [^批注：引用内容]\n" +
                "---\n" +
                "> 后台地址： /admin。 使用markdown书写，支持标准markdown语法，并扩展了表情，代码高亮。封装的markdown编辑器支持 " +
                "表情，图片（上传），链接，代码，全屏，预览等功能。响应式布局，不管是前端还是后台，自动适配各种尺寸的屏幕。\n" +
                "\n" +
                "\n" +
                "```lang-markdown\n" +
                "> 后台地址： /admin。 使用markdown书写，支持标准markdown语法，并扩展了表情，代码高亮。封装的markdown编辑器支持 " +
                "表情，图片（上传），链接，代码，全屏，预览等功能。响应式布局，不管是前端还是后台，自动适配各种尺寸的屏幕。\n" +
                "```\n" +
                "\n" +
                "### 厉害了我的列表\n" +
                "---\n" +
                "无序列表 @[微笑]\n" +
                " * 项目1\n" +
                " - 项目2\n" +
                "\n" +
                "```lang-markdown\n" +
                " * 项目1\n" +
                " - 项目2\n" +
                "```\n" +
                "\n" +
                "---\n" +
                "有序列表\n" +
                " 1. 项目1\n" +
                " 2. 项目2\n" +
                "\n" +
                "```lang-markdown\n" +
                " 1. 项目1\n" +
                " 2. 项目2\n" +
                "```\n" +
                "\n" +
                "---\n" +
                "嵌套\n" +
                " * 项目1\n" +
                "   1. 嵌套1\n" +
                "   2. 嵌套2\n" +
                "\n" +
                "```lang-markdown\n" +
                " * 项目1\n" +
                "   1. 嵌套1\n" +
                "   2. 嵌套2\n" +
                "```\n" +
                "\n" +
                "---\n" +
                "checklist\n" +
                "- [x] 项目1\n" +
                "- [x] 项目2\n" +
                " - [x] 项目3\n" +
                " - [x] 项目4\n" +
                " - [ ]  空\n" +
                "  - [x] 项目6\n" +
                "- [ ]   空\n" +
                "- [x] 项目8\n" +
                "\n" +
                "\n" +
                "```lang-markdown\n" +
                "- [x] 项目1\n" +
                "- [x] 项目2\n" +
                " - [x] 项目3\n" +
                " - [x] 项目4\n" +
                " - [ ]  空\n" +
                "  - [x] 项目6\n" +
                "- [ ]   空\n" +
                "- [x] 项目8\n" +
                "```\n" +
                "\n" +
                "\n" +
                "---\n" +
                "在引用中使用列表\n" +
                "> * 引用列表\n" +
                ">  * 项目1\n" +
                ">  * 项目2 \n" +
                "\n" +
                "```lang-markdown\n" +
                "> * 引用列表\n" +
                ">  * 项目1\n" +
                ">  * 项目2 \n" +
                "```\n" +
                "\n" +
                "### 表情\n" +
                "---\n" +
                "@[微笑]@[嘻嘻]@[哈哈]@[可爱]@[可怜]@[挖鼻]@[吃惊]@[害羞]\n" +
                "\n" +
                "```lang-markdown\n" +
                "@[微笑]@[嘻嘻]@[哈哈]@[可爱]@[可怜]@[挖鼻]@[吃惊]@[害羞]\n" +
                "```\n" +
                "\n" +
                "### 链接\n" +
                "---\n" +
                "http://miao.blog\n" +
                "[MIAO.BLOG](http://miao.blog)\n" +
                "[邮件](mailto:xoxo@ooxx.me)\n" +
                "\n" +
                "```lang-markdown\n" +
                "http://miao.blog\n" +
                "[MIAO.BLOG](http://miao.blog)\n" +
                "[邮件](mailto:xoxo@ooxx.me)\n" +
                "```\n" +
                "\n" +
                "\n" +
                "### 图片\n" +
                "---\n" +
                "![MIAO](/images/10a8ec7ad56e4dc7.png)\n" +
                "\n" +
                "```lang-markdown\n" +
                "![MIAO](/images/10a8ec7ad56e4dc7.png)\n" +
                "```\n" +
                "\n" +
                "\n" +
                "### 代码\n" +
                "---\n" +
                "通过添加 `lang-***` 可以设置代码高亮 `lang-css` `lang-js` `lang-html`\n" +
                "```lang-css\n" +
                "@media print{\n" +
                "    .no-print{\n" +
                "        display: none;\n" +
                "        height: 0;\n" +
                "    }\n" +
                "    a:after {\n" +
                "        content: \"\" !important;\n" +
                "    }\n" +
                "}\n" +
                "```\n" +
                "\n" +
                "### 表格\n" +
                "---\n" +
                "标题1 | 标题2 | 标题3\n" +
                ":----------- | :-----------: | -----------:\n" +
                "Left         | Center        | Right\n" +
                "Left         | Center        | Right\n" +
                "\n" +
                "```lang-markdown\n" +
                "标题1 | 标题2 | 标题3\n" +
                ":----------- | :-----------: | -----------:\n" +
                "Left         | Center        | Right\n" +
                "Left         | Center        | Right\n" +
                "```\n");
        svnFileDto.setParentPath("trunk");
        svnFileDto.setExtension("md");
        svnFileDto.setCommitRevision(1);

        return svnFileDto;
    }

    /**
     * check if the repository name exists
     *
     * @param projectId project ID
     * @param name      repository name
     * @return boolean
     */
    private boolean repositoryNameExists(Integer projectId, String name) {
        HashMap<String, Object> params = new HashMap<>(2);
        params.put("projectId", projectId);
        params.put("name", name);
        return commonDAO.checkExists("Repository.selectCountByName", params);
    }

    /**
     * check if the repository url name exists
     *
     * @param projectId project ID
     * @param urlName   repository name
     * @return boolean
     */
    private boolean repositoryUrlNameExists(Integer projectId, String urlName) {
        HashMap<String, Object> params = new HashMap<>(2);
        params.put("projectId", projectId);
        params.put("urlName", urlName);
        return commonDAO.checkExists("Repository.selectCountByUrlName", params);
    }
}
