package com.jie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jie.domain.dto.MenuListDto;
import com.jie.domain.entity.Menu;
import com.jie.domain.vo.MenuVo;

import java.util.List;

/**
 * 菜单权限表(Menu)表服务接口
 *
 * @author makejava
 * @since 2023-06-08 18:29:44
 */
public interface MenuService extends IService<Menu> {


    /**
     * 查询当前用户所拥有的权限信息
     * @param id
     * @return
     */
    List<String> selectPermissionsByUserId(Long id);

    List<MenuVo> selectRouterMenuTreeByUserId(Long id);

    List<Menu> selectMenuList(MenuListDto menuListDto);

    boolean hasChild(Long menuId);

    List<Long> selectMenuListByRoleId(Long roleId);
}

