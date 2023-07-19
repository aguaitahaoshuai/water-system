package com.jie.service.impl;

import com.jie.utils.SecurityUtils;
import org.springframework.stereotype.Service;

@Service("ps")
public class Permission {

    /**
     * 判断用户是否拥有该权限，如果有则返回true
     * @param permission
     * @return
     */
    public boolean hasPermissions(String permission){

        // 判断用户是不是超级管理员，是直接就返回true
        if (SecurityUtils.isAdmin()){
            return true;
        }

        // 否则就查询当前登录用户是否拥有该权限
        return SecurityUtils.getLoginUser().getPermissions().contains(permission);
    }
}
