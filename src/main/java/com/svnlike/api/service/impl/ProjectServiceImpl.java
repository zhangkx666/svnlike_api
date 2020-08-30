package com.svnlike.api.service.impl;

import com.svnlike.api.model.dto.project.request.ProjectConditionDTO;
import com.svnlike.api.model.dto.project.request.ProjectInputDTO;
import com.svnlike.api.model.entity.Project;
import com.svnlike.api.model.entity.ProjectLike;
import com.svnlike.api.service.IProjectService;
import com.svnlike.utils.exception.NotFoundException;
import com.svnlike.utils.exception.SvnLikeException;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

/**
 * @author zhangkx
 */
@Service
public class ProjectServiceImpl extends BaseService implements IProjectService {


    /**
     * get project list
     *
     * @param condition search condition
     * @return List of Project
     */
    @Override
    public List<Project> getProjectList(ProjectConditionDTO condition) {
        return commonDAO.queryForList("Project.selectMany", condition);
    }

    /**
     * create a new project
     *
     * @param input ProjectInputDTO
     * @return Project
     */
    @Override
    public Project createProject(ProjectInputDTO input) {

        // check if project name exists
        if (projectNameExists(input.getName())) {
            throw new SvnLikeException(m.error("project.name.duplicate.create", input.getName()));
        }

        // check if project url name exists
        if (projectUrlNameExists(input.getUrlName())) {
            throw new SvnLikeException(m.error("project.urlName.duplicate.create", input.getUrlName()));
        }

        // save project to db
        Project project = input.convertTo(Project.class);
        project.setCreatedBy(uvo.getName());
        project.setOwner(uvo.getName());

        commonDAO.execute("Project.add", project);
        return project;
    }

    /**
     * like a project
     *
     * @param projectId project id
     */
    @Override
    public void likeProject(Integer projectId) {
        ProjectLike projectLike = new ProjectLike(projectId, uvo.getId());
        commonDAO.execute("ProjectLike.add", projectLike);
    }

    /**
     * unlike a project
     *
     * @param projectId project id
     */
    @Override
    public void unlikeProject(Integer projectId) {
        ProjectLike projectLike = new ProjectLike(projectId, uvo.getId());
        commonDAO.execute("ProjectLike.delete", projectLike);
    }


    /**
     * get project like list
     *
     * @return the project ids liked
     */
    @Override
    public List<Integer> getProjectLikeList() {
        HashMap<String, Object> params = new HashMap<>(1);
        params.put("userId", uvo.getId());
        return commonDAO.queryForList("ProjectLike.selectMany", params);
    }

    /**
     * get project by url name
     *
     * @param urlName the url name of project
     * @return project
     */
    @Override
    public Project getProjectByUrlName(String urlName) {
        HashMap<String, Object> params = new HashMap<>(1);
        params.put("urlName", urlName);
        Project project = commonDAO.queryForObject("Project.selectOneByUrlName", params);
        if (project == null) {
            throw new NotFoundException(m.error("project.urlName.notfound", urlName));
        }
        return project;
    }

    /**
     * check if the project name exists
     *
     * @param name repository name
     * @return boolean
     */
    private boolean projectNameExists(String name) {
        HashMap<String, Object> params = new HashMap<>(1);
        params.put("name", name);
        return commonDAO.checkExists("Project.selectCountByName", params);
    }

    /**
     * check if the project name exists
     *
     * @param urlName project url name
     * @return boolean
     */
    private boolean projectUrlNameExists(String urlName) {
        HashMap<String, Object> params = new HashMap<>(1);
        params.put("urlName", urlName);
        return commonDAO.checkExists("Project.selectCountByUrlName", params);
    }
}
