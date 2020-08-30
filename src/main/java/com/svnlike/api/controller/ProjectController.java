package com.svnlike.api.controller;

import com.svnlike.api.model.dto.JsonResult;
import com.svnlike.api.model.dto.ResponseDTO;
import com.svnlike.api.model.dto.project.request.ProjectConditionDTO;
import com.svnlike.api.model.dto.project.request.ProjectInputDTO;
import com.svnlike.api.model.dto.project.response.ProjectForSelectListOutputDTO;
import com.svnlike.api.model.dto.project.response.ProjectOutputDTO;
import com.svnlike.api.model.entity.Project;
import com.svnlike.api.service.IProjectService;
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

    @Autowired
    public ProjectController(IProjectService projectService) {
        this.projectService = projectService;
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
            return outputList;

        } else {
            projectList.forEach(item -> outputList.add(item.convertTo(ProjectOutputDTO.class)));
            return outputList;
        }
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
     * Get project by project url name
     *
     * @param urlName project name
     * @return JsonResult
     */
    @RequestMapping(method = RequestMethod.GET, value = "/{urlName}")
    public ProjectOutputDTO show(@PathVariable(value = "urlName") String urlName) throws InterruptedException {
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
    public List<Integer> index() {
        return projectService.getProjectLikeList();
    }
}
