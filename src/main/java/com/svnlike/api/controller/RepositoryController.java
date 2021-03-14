package com.svnlike.api.controller;

import com.svnlike.api.model.dto.JsonResult;
import com.svnlike.api.model.dto.repository.request.RepositoryConditionDTO;
import com.svnlike.api.model.dto.repository.request.RepositoryFileDTO;
import com.svnlike.api.model.dto.repository.request.RepositoryInputDTO;
import com.svnlike.api.model.dto.repository.request.RepositoryTreeConditionDTO;
import com.svnlike.api.model.dto.repository.response.RepositoryDTO;
import com.svnlike.api.model.entity.SVNFile;
import com.svnlike.api.model.entity.SVNTreeItem;
import com.svnlike.api.model.po.RepositoryPO;
import com.svnlike.api.service.IRepositoryService;
import com.svnlike.utils.exception.SvnLikeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

/**
 * repository controller
 *
 * @author zhangkx
 */
@RestController
@RequestMapping("/repository")
public class RepositoryController extends BaseController {

    private final IRepositoryService repositoryService;

    @Autowired
    public RepositoryController(IRepositoryService repositoryService) {
        this.repositoryService = repositoryService;
    }

    /**
     * Get repositories
     *
     * @return JsonResult
     */
    @RequestMapping(method = RequestMethod.GET)
    public List<RepositoryDTO> index(RepositoryConditionDTO input) {
        List<RepositoryPO> repositoryPOList = repositoryService.getRepositoryList(input);
        List<RepositoryDTO> repositoryDTOList = new ArrayList<>();
        repositoryPOList.forEach(item -> repositoryDTOList.add(item.convertTo(RepositoryDTO.class)));

        System.out.println("******************************************");
        System.out.println(System.getProperty("sun.jnu.encoding"));
        return repositoryDTOList;
    }

    /**
     * Get repository by repository name
     *
     * @param projectUrlName    project name
     * @param repositoryUrlName repository name
     * @return JsonResult
     */
    @RequestMapping(method = RequestMethod.GET, value = "/{projectUrlName}/{repositoryUrlName}")
    public RepositoryPO show(@PathVariable(value = "projectUrlName") String projectUrlName,
                             @PathVariable(value = "repositoryUrlName") String repositoryUrlName) {
        return repositoryService.getRepositoryByUrlName(projectUrlName, repositoryUrlName);
    }

    /**
     * Get repository tree
     *
     * @param name repository name
     * @return JsonResult
     */
    @RequestMapping(method = RequestMethod.GET, value = "/{name}/tree")
    public SVNTreeItem tree(@PathVariable("name") String name, RepositoryTreeConditionDTO input) {
        return repositoryService.getRepositoryTreeByName(name, input.getPath(), input.getGetAll());
    }

    /**
     * get file
     *
     * @param name  repository name
     * @param input RepositoryFileDTO
     * @return JsonResult
     */
    @RequestMapping(method = RequestMethod.GET, value = "/{name}/file")
    public SVNFile file(@PathVariable("name") String name, RepositoryFileDTO input) {
        return repositoryService.getRepositoryFile(name, input.getPath());
    }

    /**
     * Create repository
     *
     * @return JsonResult
     */
    @RequestMapping(method = RequestMethod.POST)
    public JsonResult create(@Valid @RequestBody RepositoryInputDTO input) {
        repositoryService.createRepository(input);
        return new JsonResult(m.success("repository.create.success"));
    }

    /**
     * Update repository
     *
     * @param id    repository id
     * @param input parameters
     * @return JsonResult
     * @throws SvnLikeException ex
     */
    @Transactional
    @RequestMapping(method = RequestMethod.PATCH, value = "/{id}")
    public JsonResult update(@PathVariable("id") int id,
                             @Valid @RequestBody RepositoryInputDTO input) {
        repositoryService.updateRepositoryById(id, input);
        return new JsonResult(m.success("repository.update.success"));
    }

    /**
     * Delete Repository
     *
     * @return JsonResult
     * @throws SvnLikeException ex
     */
    @RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
    public JsonResult delete(@PathVariable("id") int id) {
        repositoryService.deleteRepositoryById(id);
        return new JsonResult(m.success("repository.delete.success"));
    }
}
