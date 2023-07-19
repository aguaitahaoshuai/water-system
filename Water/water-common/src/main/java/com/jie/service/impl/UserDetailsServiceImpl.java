package com.jie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.jie.domain.entity.LoginUser;
import com.jie.domain.entity.User;
import com.jie.mapper.UserMapper;
import com.jie.service.MenuService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Objects;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Resource
    private UserMapper userMapper;

    @Resource
    private MenuService menuService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //根据用户名查询用户信息
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUserName,username);
        User user = userMapper.selectOne(queryWrapper);
        //判断是否查到用户  如果没查到抛出异常
        if(Objects.isNull(user)){
            throw new RuntimeException("用户不存在");
        }
        //返回用户信息
        // TODO 查询权限信息封装
        // 如果是后台登录就直接返回权限信息。
        // 1 表示管理员
//        if (! user.getType().equals("0")){
//
//            List<String> permissions = menuService.selectPermissionsByUserId(user.getId());
//
//            return new LoginUser(user,permissions);
//        }

        return new LoginUser(user,menuService.selectPermissionsByUserId(user.getId()));
    }
}
