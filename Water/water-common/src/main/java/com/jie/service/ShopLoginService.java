package com.jie.service;


import com.jie.domain.ResponseResult;
import com.jie.domain.entity.User;


public interface ShopLoginService {

    /**
     * 登录
     * @param user
     * @return
     */
    ResponseResult login(User user);

    /**
     * 退出
     * @return
     */
    ResponseResult logout();
}
