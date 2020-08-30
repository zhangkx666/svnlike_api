package com.svnlike.api.model.dto.repository.request;

import com.svnlike.api.model.dto.RequestDTO;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class VoucherInputDTO extends RequestDTO {

    private String account;

    private String password;

    private MultipartFile dataFile;
}
