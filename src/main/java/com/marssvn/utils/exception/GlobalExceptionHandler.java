package com.marssvn.utils.exception;

import com.marssvn.api.model.dto.JsonResult;
import com.marssvn.utils.message.Message;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;

@RestControllerAdvice
public class GlobalExceptionHandler {

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
        e.printStackTrace();
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
        HashMap<String, String> message = new HashMap<>();
        message.put("type", Message.MESSAGE_TYPE_ERROR);
        message.put("field", fieldError.getField());
        message.put("content", fieldError.getDefaultMessage());
        jsonResult.setMessage(message);
        e.printStackTrace();
        return jsonResult;
    }
}
