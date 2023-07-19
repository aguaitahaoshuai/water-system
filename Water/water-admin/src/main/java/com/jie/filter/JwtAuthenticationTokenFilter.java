package com.jie.filter;

import com.alibaba.fastjson.JSON;
import com.jie.constants.SystemConstants;
import com.jie.domain.ResponseResult;
import com.jie.domain.entity.LoginUser;
import com.jie.enums.AppHttpCodeEnum;
import com.jie.utils.JwtUtil;
import com.jie.utils.RedisCache;
import com.jie.utils.WebUtils;
import io.jsonwebtoken.Claims;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;


@Component
public class JwtAuthenticationTokenFilter  extends OncePerRequestFilter {

    @Resource
    private RedisCache redisCache;

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        String token = httpServletRequest.getHeader("token");

        // 如果token是null，说明该接口不需要认证
        if (!StringUtils.hasText(token)) {
            filterChain.doFilter(httpServletRequest, httpServletResponse);
            return;
        }

        // 解析token
        Claims claims = null;
        try {
            claims = JwtUtil.parseJWT(token);
        } catch (Exception e) {
            e.printStackTrace();
            // 转换失败说明token非法/token过期
            ResponseResult responseResult = ResponseResult.errorResult(AppHttpCodeEnum.NEED_LOGIN);

            WebUtils.renderString(httpServletResponse, JSON.toJSONString(responseResult));
            return;
        }

        String userId = claims.getSubject();

        // 通过 userId获取redis中的用户信息
        LoginUser loginUser = redisCache.getCacheObject(SystemConstants.REDIS_USER_ID_PREFIX_ADMIN + userId);

        if (Objects.isNull(loginUser)){
            // 说明登录过期，需要重新登陆
            ResponseResult responseResult = ResponseResult.errorResult(AppHttpCodeEnum.NEED_LOGIN);

            WebUtils.renderString(httpServletResponse, JSON.toJSONString(responseResult));
            return;
        }

        // 将用户信息存入到 SecurityContext中
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginUser, null, null);
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        // 最后放行
        filterChain.doFilter(httpServletRequest, httpServletResponse);

    }
}
