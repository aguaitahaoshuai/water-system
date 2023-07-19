package com.jie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jie.constants.SystemConstants;
import com.jie.domain.dto.MenuListDto;
import com.jie.domain.entity.Menu;
import com.jie.domain.vo.MenuVo;
import com.jie.mapper.MenuMapper;
import com.jie.service.MenuService;
import com.jie.utils.BeanCopyUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 菜单权限表(Menu)表服务实现类
 *
 * @author makejava
 * @since 2023-06-08 18:29:44
 */
@Service("menuService")
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements MenuService {

    @Override
    public List<String> selectPermissionsByUserId(Long id) {

        // 如果是管理员，返回所有的权限。否则返回所拥有的权限
        if (id == SystemConstants.ADMIN_USER_ID) {
            // TODO permissions中需要有所有菜单类型为C或者F的，状态为正常的，未被删除的权限
            LambdaQueryWrapper<Menu> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.in(Menu::getMenuType, "C", "F");
            queryWrapper.eq(Menu::getStatus, SystemConstants.MENU_STATUS);

            List<Menu> menus = list(queryWrapper);

            List<String> perms = menus.stream().map(Menu::getPerms).collect(Collectors.toList());

            return perms;
        }

        return getBaseMapper().selectPermissionsByUserId(id);
    }

    @Override
    public List<MenuVo> selectRouterMenuTreeByUserId(Long userId) {
        MenuMapper menuMapper = getBaseMapper();
        List<Menu> menus = null;
        //判断是否是管理员
        if (SystemConstants.ADMIN_USER_ID.equals(userId)) {
            //如果是 获取所有符合要求的Menu
            menus = menuMapper.selectAllRouterMenu();
        } else {
            //否则  获取当前用户所具有的Menu
            menus = menuMapper.selectRouterMenuTreeByUserId(userId);
        }

        List<MenuVo> menuVos = menus.stream()
                .map(menu -> BeanCopyUtils.copyBean(menu, MenuVo.class))
                .collect(Collectors.toList());


        //构建tree
        //先找出第一层的菜单  然后去找他们的子菜单设置到children属性中
        List<MenuVo> menuTree = builderMenuTree(menuVos, 0L);
        return menuTree;
    }

    @Override
    public List<Menu> selectMenuList(MenuListDto menuListDto) {
        // 创建查询条件
        LambdaQueryWrapper<Menu> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(StringUtils.hasText(menuListDto.getStatus()),Menu::getStatus,menuListDto.getStatus());
        queryWrapper.like(StringUtils.hasText(menuListDto.getMenuName()),Menu::getMenuName,menuListDto.getMenuName());
        //排序 parent_id和order_num
        queryWrapper.orderByAsc(Menu::getParentId,Menu::getOrderNum);
        return list(queryWrapper);
    }

    @Override
    public boolean hasChild(Long menuId) {

        LambdaQueryWrapper<Menu> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Menu::getParentId,menuId);
        return count(queryWrapper) != 0;
    }

    @Override
    public List<Long> selectMenuListByRoleId(Long roleId) {
        return getBaseMapper().selectMenuListByRoleId(roleId);
    }


    private List<MenuVo> builderMenuTree(List<MenuVo> menus, Long parentId) {
        List<MenuVo> menuTree = menus.stream()
                .filter(menu -> menu.getParentId().equals(parentId))
                .map(menu -> menu.setChildren(getChildren(menu, menus)))
                .collect(Collectors.toList());
        return menuTree;
    }

    /**
     * 获取存入参数的 子Menu集合
     *
     * @param menu
     * @param menus
     * @return
     */
    private List<MenuVo> getChildren(MenuVo menu, List<MenuVo> menus) {
        return menus.stream()
                .filter(m -> m.getParentId().equals(menu.getId()))
                .map(m -> m.setChildren(getChildren(m, menus)))
                .collect(Collectors.toList());
    }
}

