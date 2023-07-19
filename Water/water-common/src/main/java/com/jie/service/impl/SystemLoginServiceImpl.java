package com.jie.service.impl;

import com.jie.constants.SystemConstants;
import com.jie.domain.ResponseResult;
import com.jie.domain.entity.LoginUser;
import com.jie.domain.entity.User;
import com.jie.domain.vo.UserLoginVo;
import com.jie.service.SystemLoginService;
import com.jie.utils.JwtUtil;
import com.jie.utils.RedisCache;
import com.jie.utils.SecurityUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Objects;


@Service
public class SystemLoginServiceImpl implements SystemLoginService {

    @Resource
    private AuthenticationManager authenticationManager;

    @Resource
    private RedisCache redisCache;

    /**
     * 完成用户认证操作，认证成功返回响应数据
     * @param u 前端传参过来的 用户名和密码对象
     * @return
     */
    @Override
    public ResponseResult login(User u) {

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(u.getUserName(), u.getPassword());

        Authentication authenticate = authenticationManager.authenticate(authenticationToken);

        if (Objects.isNull(authenticate)){
            throw new RuntimeException("用户名或密码错误");
        }

        LoginUser loginUser = (LoginUser) authenticate.getPrincipal();

        // 获取用户id
        User user = loginUser.getUser();
        String userId = user.getId().toString();

        // 创建token
        String jwtToken = JwtUtil.createJWT(userId);

        // 将用户信息存入到redis中
        redisCache.setCacheObject(SystemConstants.REDIS_USER_ID_PREFIX_ADMIN + userId,loginUser);

        UserLoginVo userLoginVo = new UserLoginVo(jwtToken);
        // 封装响应类
        return ResponseResult.okResult(userLoginVo);
    }

    @Override
    public ResponseResult logout() {
        // 删除redis中的数据。
        Long userId = SecurityUtils.getUserId();

        // 根据登录时的key删除存的数据
        redisCache.deleteObject(SystemConstants.REDIS_USER_ID_PREFIX_ADMIN + userId);

        return ResponseResult.okResult();
    }

}
