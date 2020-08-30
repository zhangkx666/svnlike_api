package com.svnlike.api.controller;

import com.svnlike.api.model.dto.repository.request.RepositoryConditionDTO;
import com.svnlike.api.model.dto.repository.response.SvnServerDTO;
import com.svnlike.api.model.entity.SvnServer;
import com.svnlike.api.service.ISvnServerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * repository controller
 *
 * @author zhangkx
 */
@RestController
@RequestMapping("/server")
public class ServerController extends BaseController {

    @Autowired
    ISvnServerService svnServerService;

    /**
     * Get repositories
     *
     * @return JsonResult
     */
    @RequestMapping(method = RequestMethod.GET)
    public List<SvnServerDTO> index(RepositoryConditionDTO input) {
        List<SvnServer> svnServerList = svnServerService.getSvnServerList();
        List<SvnServerDTO> svnServerDTOList = new ArrayList<>();
        svnServerList.forEach(item -> svnServerDTOList.add(item.convertTo(SvnServerDTO.class)));
        return svnServerDTOList;
    }
}
