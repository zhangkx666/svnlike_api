package com.svnlike.utils.exception;

import com.svnlike.api.model.dto.JsonResult;
import com.svnlike.utils.enums.EMessageType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLSyntaxErrorException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * GlobalExceptionHandler Advice
 *
 * @author zhangkx
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * handle SvnLikeException
     *
     * @param e SvnLikeException
     * @return JsonResult
     */
    @ResponseBody
    @ExceptionHandler(SvnLikeException.class)
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    JsonResult svnLikeExceptionHandler(SvnLikeException e) {
        logger.error("SvnLikeException: " + e.getMessage());

        return e.toJsonResult();
    }

    /***
     * resource not found 404
     * @param e NotFoundException
     * @return JsonResult
     */
    @ResponseBody
    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    JsonResult notFountHandler(NotFoundException e) {
        logger.error("NotFoundException: " + e.getMessage());

        return e.toJsonResult();
    }

    /**
     * handle MethodArgumentNotValidException
     *
     * @param e MethodArgumentNotValidException
     * @return JsonResult
     */
    @ResponseBody
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    JsonResult methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException e) {
        List<FieldError> fieldErrorList = e.getBindingResult().getFieldErrors();

        List<HashMap<String, Object>> errorList = new ArrayList<>();
        String[] messages = new String[fieldErrorList.size()];

        for (int i = 0; i < fieldErrorList.size(); i++) {
            FieldError fieldError = fieldErrorList.get(i);
            HashMap<String, Object> errorFieldMap = new HashMap<>(3);
            errorFieldMap.put("field", fieldError.getField());
            errorFieldMap.put("value", fieldError.getRejectedValue());
            errorFieldMap.put("message", fieldError.getDefaultMessage());
            errorList.add(errorFieldMap);

            messages[i] = fieldError.getDefaultMessage();
        }

        String message = String.join("\n", messages);
        logger.error("VALIDATION_ERROR: " + message);
        JsonResult jsonResult = new JsonResult("validation error", message, errorList);
        jsonResult.setType(EMessageType.ERROR);
        return jsonResult;
    }

    /**
     * handle SQLSyntaxErrorException
     *
     * @param e SQLSyntaxErrorException
     * @return JsonResult
     */
    @ResponseBody
    @ExceptionHandler(SQLSyntaxErrorException.class)
    @ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
    JsonResult sqlSyntaxErrorExceptionHandler(SQLSyntaxErrorException e) {
        logger.error("SQLSyntaxErrorException: " + e.getMessage());

        JsonResult jsonResult = new JsonResult(e.getMessage());
        jsonResult.setType(EMessageType.ERROR);
        return jsonResult;
    }
}
