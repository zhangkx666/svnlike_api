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
public class SvnLikeException extends RuntimeException {

    /**
     * error Code
     */
    private String code;

    /**
     * error message
     */
    private String message;

    /**
     * New SvnLikeException by message string
     *
     * @param message message
     */
    public SvnLikeException(String message) {
        super(message);
        this.message = message;
    }

    /**
     * New SvnLikeException by Message
     *
     * @param message Message
     */
    public SvnLikeException(@NotNull Message message) {
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
