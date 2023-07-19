package com.jie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jie.constants.SystemConstants;
import com.jie.domain.ResponseResult;
import com.jie.domain.entity.Role;
import com.jie.domain.entity.RoleMenu;
import com.jie.domain.vo.PageVo;
import com.jie.mapper.RoleMapper;
import com.jie.service.RoleMenuService;
import com.jie.service.RoleService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 角色信息表(Role)表服务实现类
 *
 * @author makejava
 * @since 2023-06-08 18:29:44
 */
@Service("roleService")
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {

    @Resource
    private RoleMenuService roleMenuService;
    @Override
    public List<String> selectRoleKeyByUserId(Long userId) {

        // 判断用户所有对应的角色是不是超级管理员
        if (SystemConstants.ADMIN_USER_ID == userId){
           List<String> list = new ArrayList<>();
           list.add(SystemConstants.ADMIN_ROLE_KEY);
            return list;
        }

        // TODO 查询该用户所对应的角色信息
        return getBaseMapper().selectRoleKeyByUserId(userId);
    }

    @Override
    public ResponseResult selectRolePage(Role role, Integer pageNum, Integer pageSize) {
        LambdaQueryWrapper<Role> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        //目前没有根据id查询
//        lambdaQueryWrapper.eq(Objects.nonNull(role.getId()),Role::getId,role.getId());
        lambdaQueryWrapper.like(StringUtils.hasText(role.getRoleName()),Role::getRoleName,role.getRoleName());
        lambdaQueryWrapper.eq(StringUtils.hasText(role.getStatus()),Role::getStatus,role.getStatus());
        lambdaQueryWrapper.orderByAsc(Role::getRoleSort);

        Page<Role> page = new Page<>();
        page.setCurrent(pageNum);
        page.setSize(pageSize);
        page(page,lambdaQueryWrapper);

        //转换成VO
        List<Role> roles = page.getRecords();

        PageVo pageVo = new PageVo();
        pageVo.setTotal(page.getTotal());
        pageVo.setRows(roles);
        return ResponseResult.okResult(pageVo);
    }

    @Override
    @Transactional
    public void insertRole(Role role) {
        save(role);
        System.out.println(role.getId());
        if(role.getMenuIds()!=null&&role.getMenuIds().length>0){
            insertRoleMenu(role);
        }
    }

    @Override
    public void updateRole(Role role) {
        updateById(role);
        roleMenuService.deleteRoleMenuByRoleId(role.getId());
        insertRoleMenu(role);
    }

    @Override
    public List<Role> selectRoleAll() {
        return list(Wrappers.<Role>lambdaQuery().eq(Role::getStatus, SystemConstants.NORMAL));
    }

    @Override
    public List<Long> selectRoleIdByUserId(Long userId) {
        return getBaseMapper().selectRoleIdByUserId(userId);
    }

    private void insertRoleMenu(Role role) {
        List<RoleMenu> roleMenuList = Arrays.stream(role.getMenuIds())
                .map(memuId -> new RoleMenu(role.getId(), memuId))
                .collect(Collectors.toList());
        roleMenuService.saveBatch(roleMenuList);
    }
}

