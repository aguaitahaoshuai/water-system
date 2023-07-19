package com.jie.handler.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jie.domain.ResponseResult;
import com.jie.enums.AppHttpCodeEnum;
import com.jie.utils.WebUtils;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@Component
public class AccessDeniedHandlerImpl implements AccessDeniedHandler {

    @Resource
    private ObjectMapper objectMapper;



    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        // 打印异常信息
        accessDeniedException.printStackTrace();

        ResponseResult result = ResponseResult.errorResult(AppHttpCodeEnum.NO_OPERATOR_AUTH);
        //响应给前端
        String valueAsString = objectMapper.writeValueAsString(result);
        WebUtils.renderString(response,valueAsString);
    }
}
