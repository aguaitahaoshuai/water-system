package com.jie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jie.domain.ResponseResult;
import com.jie.domain.entity.Role;

import java.util.List;

/**
 * 角色信息表(Role)表服务接口
 *
 * @author makejava
 * @since 2023-06-08 18:29:44
 */
public interface RoleService extends IService<Role> {


    /**
     * 根据用户的id查询 角色名称
     * @param id
     * @return
     */
    List<String> selectRoleKeyByUserId(Long id);


    ResponseResult selectRolePage(Role role, Integer pageNum, Integer pageSize);

    void insertRole(Role role);

    void updateRole(Role role);

    List<Role> selectRoleAll();

    List<Long> selectRoleIdByUserId(Long userId);

}

