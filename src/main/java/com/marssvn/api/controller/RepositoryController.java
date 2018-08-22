package com.marssvn.api.controller;

import com.marssvn.api.model.dto.JsonResult;
import com.marssvn.api.model.dto.repository.request.RepositoryConditionDTO;
import com.marssvn.api.model.dto.repository.request.RepositoryInputDTO;
import com.marssvn.api.model.dto.repository.request.RepositoryTreeConditionDTO;
import com.marssvn.api.model.dto.repository.response.RepositoryDTO;
import com.marssvn.api.model.po.RepositoryPO;
import com.marssvn.api.service.business.IRepositoryService;
import com.marssvn.utils.exception.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/repository")
public class RepositoryController extends BaseController {

    @Autowired
    private IRepositoryService repositoryService;

    /**
     * Get repositories
     *
     * @return JsonResult
     */
    @RequestMapping(method = RequestMethod.GET)
    public JsonResult index(RepositoryConditionDTO input) {
        List<RepositoryPO> repositoryPOList = repositoryService.getRepositoryList(input);
        List<RepositoryDTO> repositoryDTOList = new ArrayList<>();
        repositoryPOList.forEach(item -> repositoryDTOList.add(item.convertTo(RepositoryDTO.class)));
        return new JsonResult(repositoryDTOList);
    }

    /**
     * Get repository by repository id
     *
     * @param id repository id
     * @return JsonResult
     */
    @RequestMapping(method = RequestMethod.GET, value = "/{id}")
    public JsonResult show(@PathVariable(value = "id") int id) {
        RepositoryPO repositoryPO = repositoryService.getRepositoryById(id);
        if (repositoryPO == null)
            throw new BusinessException(message.error("repository.not_exists", String.valueOf(id)));

        return new JsonResult(repositoryPO.convertTo(RepositoryDTO.class));
    }

    /**
     * Get repository tree
     *
     * @param id repository id
     * @return JsonResult
     */
    @RequestMapping(method = RequestMethod.GET, value = "/{id}/tree")
    public JsonResult tree(@PathVariable("id") int id, RepositoryTreeConditionDTO input) {
        return new JsonResult(repositoryService.getRepositoryTreeById(id, input.getPath()));
    }

    /**
     * Create repository
     *
     * @return JsonResult
     */
    @RequestMapping(method = RequestMethod.POST)
    public JsonResult create(@Valid @RequestBody RepositoryInputDTO input) {

        // create repository
        int repositoryId = repositoryService.createRepository(input);

        // output
        HashMap<String, Integer> output = new HashMap<>();
        output.put("id", repositoryId);

        // json result
        JsonResult jsonResult = new JsonResult();
        jsonResult.setData(output);
        jsonResult.setMessage(message.success("repository.create.success"));
        return jsonResult;
    }

    /**
     * Update repository
     *
     * @param id    repository id
     * @param input parameters
     * @return JsonResult
     * @throws BusinessException ex
     */
    @Transactional
    @RequestMapping(method = RequestMethod.PATCH, value = "/{id}")
    public JsonResult update(@PathVariable("id") int id,
                             @Valid @RequestBody RepositoryInputDTO input) {
        repositoryService.updateRepositoryById(id, input);
        return new JsonResult(message.success("repository.update.success"));
    }

    /**
     * Delete Repository
     *
     * @return JsonResult
     * @throws BusinessException ex
     */
    @RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
    public JsonResult delete(@PathVariable("id") int id) {
        repositoryService.deleteRepositoryById(id);
        return new JsonResult(message.success("repository.delete.success"));
    }
}
