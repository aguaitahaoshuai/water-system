package com.jie.handler.exception;

import com.jie.domain.ResponseResult;
import com.jie.enums.AppHttpCodeEnum;
import com.jie.exception.SystemException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.stream.Collectors;


@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * 字段校验异常
     * @param e
     * @return
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseResult systemExceptionHandler(MethodArgumentNotValidException e) {

        List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors();

        String errorMessage = fieldErrors.stream().map(s -> s.getDefaultMessage()).collect(Collectors.joining(";"));

        log.error("出现异常了，{}",errorMessage);
        return ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR.getCode(),errorMessage);
    }

    @ExceptionHandler(SystemException.class)
    public ResponseResult systemExceptionHandler(SystemException exception) {

        log.error("出现异常了！ {}",exception);

        return ResponseResult.errorResult(exception.getCode(),exception.getMessage());

    }

    @ExceptionHandler(Exception.class)
    public ResponseResult systemExceptionHandler(Exception exception) {

        log.error("出现异常了！ {}",exception);

        return ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR.getCode(),exception.getMessage());

    }
}
