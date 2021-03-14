package com.svnlike.api.controller;

import com.svnlike.utils.common.UtilsX;
import com.svnlike.utils.exception.SvnLikeException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

/**
 * @author zhangkx
 */
@RestController
@RequestMapping("/upload")
public class UploadController extends BaseController {

    @Value("${file.uploadFolder}")
    private String uploadFolder;

    /**
     *
     * @param avatarFile file
     * @param type p: project, r: repository, u: user, t: team
     * @param dataId data id
     * @return JsonResult
     */
    @RequestMapping(method = RequestMethod.POST, value = "/avatar")
    public String uploadAvatar(@RequestParam("avatarFile") MultipartFile avatarFile,
                           @RequestParam("type") String type,
                           @RequestParam("dataId") String dataId) {
        if (avatarFile.isEmpty()) {
            throw new SvnLikeException("Empty avatar file");
        }

        String fileName = avatarFile.getOriginalFilename();
        String suffix = fileName.substring(fileName.lastIndexOf("."));
        fileName = type.substring(0, 1) + dataId + "_" + UtilsX.md5(dataId) + suffix;

        File dest = new File(uploadFolder + "\\avatar\\" + type + "\\" + fileName);
        if (!dest.getParentFile().exists()) {
            dest.getParentFile().mkdirs();
        }

        try {
            avatarFile.transferTo(dest);

        } catch (IOException e) {
            throw new SvnLikeException(e.getMessage());
        }

        return "avatar/" + type + "/" + fileName;
    }
}
