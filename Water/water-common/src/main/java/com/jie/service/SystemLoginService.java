package com.jie.service;

import com.jie.domain.ResponseResult;
import com.jie.domain.entity.User;


public interface SystemLoginService {

    /**
     * 登录
     * @param user
     * @return
     */
    ResponseResult login(User user);


    /**
     * 系统登录退出
     * @return
     */
    ResponseResult logout();
}
