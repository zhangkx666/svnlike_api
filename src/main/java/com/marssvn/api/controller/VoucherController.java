package com.marssvn.api.controller;

import com.marssvn.api.model.dto.repository.request.VoucherInputDTO;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * Test
 *
 * @author zhangkx
 */
@RestController
@RequestMapping("/voucher")
public class VoucherController extends BaseController {

    /**
     * /voucher/upload
     * @param input
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, value = "/upload")
    public Map<String, Object> upload(VoucherInputDTO input) {

        System.out.println(input.getAccount());
        System.out.println(input.getPassword());
        System.out.println(input.getDataFile().getName());
        System.out.println(input.getDataFile().getSize());
        System.out.println(input.getDataFile().getContentType());


        Map<String, Object> resultMap = new HashMap<>(2);
        resultMap.put("status", "success");
        resultMap.put("message", "账号密码验证失败");

        return resultMap;
    }
}
