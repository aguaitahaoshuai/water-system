package com.jie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jie.domain.entity.Menu;
import org.apache.ibatis.annotations.Param;

import java.util.List;


/**
 * 菜单权限表(Menu)表数据库访问层
 *
 * @author makejava
 * @since 2023-06-08 18:29:44
 */
public interface MenuMapper extends BaseMapper<Menu> {

    /**
     * 查询用户的权限
     * @param userId
     * @return
     */
    List<String> selectPermissionsByUserId(@Param("userId") Long userId);

    List<Menu> selectAllRouterMenu();

    List<Menu> selectRouterMenuTreeByUserId(Long userId);

    List<Long> selectMenuListByRoleId(Long roleId);
}
