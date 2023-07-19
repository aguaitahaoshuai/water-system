package com.jie.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jie.domain.entity.Role;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 角色信息表(Role)表数据库访问层
 *
 * @author makejava
 * @since 2023-06-08 18:29:42
 */
public interface RoleMapper extends BaseMapper<Role> {


    /**
     * // 查询 用户角色表所关联的 用户所对应的角色id，再根据角色id查询 RoleKey
     * @param userId 用户id
     * @return
     */
    List<String> selectRoleKeyByUserId(@Param("userId") Long userId);

    List<Long> selectRoleIdByUserId(Long userId);
}
