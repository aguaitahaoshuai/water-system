package com.jie.service.impl;

import com.jie.constants.SystemConstants;
import com.jie.domain.ResponseResult;
import com.jie.domain.vo.BLogUserLoginVo;
import com.jie.domain.entity.LoginUser;
import com.jie.domain.entity.User;
import com.jie.domain.vo.LogoutVo;
import com.jie.domain.vo.UserInfoVo;
import com.jie.enums.AppHttpCodeEnum;
import com.jie.service.ShopLoginService;
import com.jie.utils.BeanCopyUtils;
import com.jie.utils.JwtUtil;
import com.jie.utils.RedisCache;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Objects;

@Service
public class ShopLoginServiceImpl implements ShopLoginService {


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
        redisCache.setCacheObject(SystemConstants.REDIS_USER_ID_PREFIX_SHOP + userId,user);

        // user转换UserInfoVo
        UserInfoVo userInfoVo = BeanCopyUtils.copyBean(user, UserInfoVo.class);

        BLogUserLoginVo bLogUserLoginVo = new BLogUserLoginVo(jwtToken, JwtUtil.JWT_TTL,userInfoVo, AppHttpCodeEnum.LOGIN_SUCCESS.getMsg(), AppHttpCodeEnum.SUCCESS.getCode());
        // 封装响应类
        return ResponseResult.okResult(bLogUserLoginVo);
    }

    @Override
    public ResponseResult logout() {

        // 获取token解析用户的id
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        User user = (User) authentication.getPrincipal();


        // 获取用户id
        Long userId = user.getId();

        // 删除redis中的用户信息
        redisCache.deleteObject(SystemConstants.REDIS_USER_ID_PREFIX_SHOP + userId);

        return ResponseResult.okResult(new LogoutVo(AppHttpCodeEnum.LOGOUT_SUCCESS.getCode(),AppHttpCodeEnum.LOGOUT_SUCCESS.getMsg()));
    }
}
