package com.jie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jie.domain.ResponseResult;
import com.jie.domain.dto.UserInfoDto;
import com.jie.domain.dto.UserRegisterDto;
import com.jie.domain.entity.User;

public interface UserService extends IService<User> {

    /**
     * 获取用户信息
     * @return
     */
    ResponseResult getUserInfo();

    /**
     * 更新用户信息
     * @param userInfo
     * @return
     */
    ResponseResult updateUser(UserInfoDto userInfo);


    /**
     * 注册用户
     * @param userRegisterDto
     * @return
     */
    ResponseResult register(UserRegisterDto userRegisterDto);

    ResponseResult selectUserPage(User user, Integer pageNum, Integer pageSize);

    boolean checkUserNameUnique(String userName);

    boolean checkPhoneUnique(User user);

    boolean checkEmailUnique(User user);

    ResponseResult addUser(User user);

    void updateUser(User user);
}
