package com.marssvn.utils.exception;

import com.marssvn.api.model.dto.JsonResult;
import com.marssvn.utils.message.Message;
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
import java.util.HashMap;

/**
 * GlobalExceptionHandler Advice
 * @author zhangkx
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * handle business exception
     *
     * @param e BusinessException
     * @return JsonResult
     */
    @ResponseBody
    @ExceptionHandler(BusinessException.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    JsonResult businessExceptionHandler(BusinessException e) {
        JsonResult jsonResult = new JsonResult();
        jsonResult.setStatus(0);
        jsonResult.setErrorCode("BUSINESS_ERROR");
        jsonResult.setMessage(Message.MESSAGE_TYPE_ERROR, e.getMessage());
        logger.error("BUSINESS_ERROR: " + e.getMessage());
//        e.printStackTrace();
        return jsonResult;
    }

    /**
     * handle MethodArgumentNotValidException
     * @param e MethodArgumentNotValidException
     * @return JsonResult
     */
    @ResponseBody
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    JsonResult methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException e) {
        FieldError fieldError = e.getBindingResult().getFieldErrors().get(0);
        JsonResult jsonResult = new JsonResult();
        jsonResult.setStatus(0);
        jsonResult.setErrorCode("VALIDATION_ERROR");
        HashMap<String, String> message = new HashMap<>(3);
        message.put("type", Message.MESSAGE_TYPE_ERROR);
        message.put("field", fieldError.getField());
        message.put("content", fieldError.getDefaultMessage());
        jsonResult.setMessage(message);
//        e.printStackTrace();
        logger.error("VALIDATION_ERROR: " + fieldError.getDefaultMessage());
        return jsonResult;
    }

    /**
     * handle SQLSyntaxErrorException
     * @param e SQLSyntaxErrorException
     * @return JsonResult
     */
    @ResponseBody
    @ExceptionHandler(SQLSyntaxErrorException.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    JsonResult sqlSyntaxErrorExceptionHandler(SQLSyntaxErrorException e) {
        JsonResult jsonResult = new JsonResult();
        jsonResult.setStatus(0);
        jsonResult.setErrorCode("DB_ERROR");
        jsonResult.setMessage(Message.MESSAGE_TYPE_ERROR, e.getMessage());
        e.printStackTrace();
        return jsonResult;
    }
}
