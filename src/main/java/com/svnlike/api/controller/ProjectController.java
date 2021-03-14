package com.svnlike.api.controller;

import com.svnlike.api.model.dto.JsonResult;
import com.svnlike.api.model.dto.ResponseDTO;
import com.svnlike.api.model.dto.project.request.ProjectConditionDTO;
import com.svnlike.api.model.dto.project.request.ProjectInputDTO;
import com.svnlike.api.model.dto.project.response.ProjectForSelectListOutputDTO;
import com.svnlike.api.model.dto.project.response.ProjectOutputDTO;
import com.svnlike.api.model.dto.repository.request.RepositoryInputDTO;
import com.svnlike.api.model.dto.repository.response.RepositoryDTO;
import com.svnlike.api.model.entity.Project;
import com.svnlike.api.model.entity.Repository;
import com.svnlike.api.model.po.RepositoryPO;
import com.svnlike.api.service.IProjectService;
import com.svnlike.api.service.IRepositoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;


/**
 * project controller
 *
 * @author zhangkx
 */
@RestController
@RequestMapping("/project")
public class ProjectController extends BaseController {

    private final IProjectService projectService;
    private final IRepositoryService repositoryService;

    @Autowired
    public ProjectController(IProjectService projectService, IRepositoryService repositoryService) {
        this.projectService = projectService;
        this.repositoryService = repositoryService;
    }

    /**
     * Get repositories
     *
     * @return JsonResult
     */
    @RequestMapping(method = RequestMethod.GET)
    public List<ResponseDTO> index(ProjectConditionDTO condition) {
        List<Project> projectList = projectService.getProjectList(condition);
        List<ResponseDTO> outputList = new ArrayList<>();

        // if for select list, just return id, urlName, visibility
        if (condition.isForSelect()) {
            projectList.forEach(item -> outputList.add(item.convertTo(ProjectForSelectListOutputDTO.class)));

        } else {
            projectList.forEach(item -> outputList.add(item.convertTo(ProjectOutputDTO.class)));
        }
        return outputList;
    }

    /**
     * Create project
     *
     * @return JsonResult
     */
    @RequestMapping(method = RequestMethod.POST)
    public JsonResult create(@Valid @RequestBody ProjectInputDTO input) {

        // create repository
        Project project = projectService.createProject(input);
        ProjectOutputDTO output = project.convertTo(ProjectOutputDTO.class);
        return new JsonResult(m.success("project.create.success"), output);
    }

    /**
     * Create project
     *
     * @return JsonResult
     */
    @RequestMapping(method = RequestMethod.PUT, value = "/{id}")
    public JsonResult update(@PathVariable("id") int id,
                             @Valid @RequestBody ProjectInputDTO input) {
        projectService.updateProjectById(id, input);
        return new JsonResult(m.success("project.update.success"));
    }

    /**
     * Get project by project url name
     *
     * @param urlName project name
     * @return JsonResult
     */
    @RequestMapping(method = RequestMethod.GET, value = "/{urlName}")
    public ProjectOutputDTO show(@PathVariable(value = "urlName") String urlName) {
        Project project = projectService.getProjectByUrlName(urlName);
        return project.convertTo(ProjectOutputDTO.class);
    }

    /**
     * like a project
     * post: project/:id/like
     *
     * @param projectId project id
     * @return JsonResult
     */
    @RequestMapping(method = RequestMethod.POST, value = "/{projectId}/like")
    public JsonResult like(@PathVariable("projectId") Integer projectId) {
        projectService.likeProject(projectId);
        return new JsonResult(m.success("project.like.success"));
    }

    /**
     * unlike a project
     * post project/:id/like
     *
     * @param projectId project id
     * @return JsonResult
     */
    @RequestMapping(method = RequestMethod.POST, value = "/{projectId}/unlike")
    public JsonResult unlike(@PathVariable("projectId") Integer projectId) {
        projectService.unlikeProject(projectId);
        return new JsonResult(m.success("project.unlike.success"));
    }

    /**
     * Get project like list
     * get: project/likedIds
     *
     * @return JsonResult
     */
    @RequestMapping(method = RequestMethod.GET, value = "/likedIds")
    public List<Integer> likedIds() {
        return projectService.getProjectLikeList();
    }

    /**
     * repositories of project
     * get project/:id/repository
     *
     * @param projectId project id
     * @return JsonResult
     */
    @RequestMapping(method = RequestMethod.GET, value = "/{projectId}/repository")
    public List<RepositoryDTO> repository(@PathVariable("projectId") Integer projectId) {
        List<RepositoryPO> repositoryPOList = repositoryService.getRepositoryListByProjectId(projectId);
        List<RepositoryDTO> repositoryDTOList = new ArrayList<>();
        repositoryPOList.forEach(item -> repositoryDTOList.add(item.convertTo(RepositoryDTO.class)));

        return repositoryDTOList;
    }
}
