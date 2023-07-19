package com.jie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jie.domain.entity.RoleMenu;

public interface RoleMenuService extends IService<RoleMenu> {

    void deleteRoleMenuByRoleId(Long id);
}
