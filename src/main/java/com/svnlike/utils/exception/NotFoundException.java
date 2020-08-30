package com.svnlike.utils.exception;

import com.svnlike.api.model.dto.JsonResult;
import com.svnlike.utils.common.Message;
import com.svnlike.utils.enums.EMessageType;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

/**
 * SvnLikeException
 *
 * @author zhangkx
 */
@Getter
@Setter
public class NotFoundException extends RuntimeException {

    /**
     * error Code
     */
    private String code;

    /**
     * error message
     */
    private String message;

    /**
     * New NotFoundException by IMessage.class
     *
     * @param message IMessage
     */
    public NotFoundException(@NotNull Message message) {
        super(message.toString());
        this.code = message.getCode();
        this.message = message.toString();
    }

    /**
     * to JsonResult
     *
     * @return JsonResult
     */
    public JsonResult toJsonResult() {
        JsonResult jsonResult = new JsonResult(this.code, this.message);
        jsonResult.setType(EMessageType.ERROR);
        return jsonResult;
    }
}
