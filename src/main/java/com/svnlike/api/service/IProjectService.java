package com.svnlike.api.service;

import com.svnlike.api.model.dto.project.request.ProjectConditionDTO;
import com.svnlike.api.model.dto.project.request.ProjectInputDTO;
import com.svnlike.api.model.entity.Project;

import java.util.List;

/**
 * @author zhangkx
 */
public interface IProjectService {

    /**
     * get project list
     *
     * @param condition search condition
     * @return List of Project
     */
    List<Project> getProjectList(ProjectConditionDTO condition);

    /**
     * create a new project
     *
     * @param input ProjectInputDTO
     * @return Project
     */
    Project createProject(ProjectInputDTO input);

    /**
     * like a project
     *
     * @param projectId project id
     */
    void likeProject(Integer projectId);

    /**
     * unlike a project
     *
     * @param projectId project id
     */
    void unlikeProject(Integer projectId);

    /**
     * get project like list
     *
     * @return the project ids liked
     */
    List<Integer> getProjectLikeList();

    /**
     * get project by url name
     * @param urlName the url name of project
     * @return project
     */
    Project getProjectByUrlName(String urlName);
}
