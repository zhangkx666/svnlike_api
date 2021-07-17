package com.svnlike.api.controller;

import com.svnlike.api.model.dto.JsonResult;
import com.svnlike.api.model.dto.repository.request.RepositoryFileDTO;
import com.svnlike.api.model.dto.repository.request.RepositoryInputDTO;
import com.svnlike.api.model.dto.repository.request.RepositoryTreeConditionDTO;
import com.svnlike.api.model.dto.repository.response.SvnFileDto;
import com.svnlike.api.model.dto.repository.response.SvnTreeDto;
import com.svnlike.api.model.po.RepositoryPO;
import com.svnlike.api.service.IRepositoryService;
import com.svnlike.svnapi.model.SvnEntry;
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

//    /**
//     * Get repositories
//     *
//     * @return JsonResult
//     */
//    @RequestMapping(method = RequestMethod.GET)
//    public List<RepositoryDTO> index(RepositoryConditionDTO input) {
//        List<RepositoryPO> repositoryPOList = repositoryService.getRepositoryList(input);
//        List<RepositoryDTO> repositoryDTOList = new ArrayList<>();
//        repositoryPOList.forEach(item -> repositoryDTOList.add(item.convertTo(RepositoryDTO.class)));
//
//        System.out.println("******************************************");
//        System.out.println(System.getProperty("sun.jnu.encoding"));
//        return repositoryDTOList;
//    }

    /**
     * Get repository by repository name
     * url: /repository/svnlike/api_doc
     *
     * @param projectUrlName    project name
     * @param repositoryUrlName repository name
     * @return JsonResult
     */
    @RequestMapping(method = RequestMethod.GET, value = "/{projectUrlName}/{repositoryUrlName}")
    public RepositoryPO showByUrl(@PathVariable(value = "projectUrlName") String projectUrlName,
                                  @PathVariable(value = "repositoryUrlName") String repositoryUrlName) {
        return repositoryService.getRepositoryByUrlName(projectUrlName, repositoryUrlName);
    }

    /**
     * Get repository by repository id
     * url: /repository/1
     *
     * @param repositoryId repository id
     * @return JsonResult
     */
    @RequestMapping(method = RequestMethod.GET, value = "/{repositoryId}")
    public RepositoryPO showById(@PathVariable(value = "repositoryId") Integer repositoryId) {
        return repositoryService.getRepositoryById(repositoryId);
    }

    /**
     * Get repository tree
     * /repository/1/tree?path=
     *
     * @param repositoryId repository id
     * @param input        RepositoryTreeConditionDTO
     * @return JsonResult
     */
    @RequestMapping(method = RequestMethod.GET, value = "/{repositoryId}/tree")
    public List<SvnTreeDto> tree(@PathVariable(value = "repositoryId") Integer repositoryId,
                                 RepositoryTreeConditionDTO input) {

        List<SvnEntry> list = repositoryService.getRepositoryTree(repositoryId, input.getPath());
        List<SvnTreeDto> outputList = new ArrayList<>();
        list.forEach(svnEntry -> outputList.add(svnEntry.convertTo(SvnTreeDto.class)));
        return outputList;
    }

    /**
     * get file
     * /repository/1/file?path=
     *
     * @param repositoryId repository id
     * @param input        RepositoryFileDTO
     * @return JsonResult
     */
    @RequestMapping(method = RequestMethod.GET, value = "/{repositoryId}/file")
    public SvnFileDto file(@PathVariable(value = "repositoryId") Integer repositoryId, RepositoryFileDTO input) {
        return repositoryService.getRepositoryFile(repositoryId, input.getPath());
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
