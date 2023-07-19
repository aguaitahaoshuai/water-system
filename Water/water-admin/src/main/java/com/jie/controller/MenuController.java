package com.jie.controller;

import com.jie.domain.ResponseResult;
import com.jie.domain.dto.MenuListDto;
import com.jie.domain.entity.Menu;
import com.jie.domain.vo.MenuListVo;
import com.jie.domain.vo.MenuTreeVo;
import com.jie.domain.vo.RoleMenuTreeSelectVo;
import com.jie.service.MenuService;
import com.jie.utils.BeanCopyUtils;
import com.jie.utils.SystemConverter;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
public class MenuController {

    @Resource
    private MenuService menuService;

    /**
     * 查询菜单列表
     *
     * @param menuListDto
     * @return
     */
    @RequestMapping(value = "/system/menu/list", method = RequestMethod.GET)
    public ResponseResult list(MenuListDto menuListDto) {

        List<Menu> menus = menuService.selectMenuList(menuListDto);

        // 封装vo
        List<MenuListVo> menuListVos = BeanCopyUtils.copyBeanList(menus, MenuListVo.class);
        return ResponseResult.okResult(menuListVos);

    }

    /**
     * 新增菜单
     *
     * @param menu
     * @return
     */
    @PostMapping("/system/menu")
    public ResponseResult add(@RequestBody Menu menu) {
        menuService.save(menu);
        return ResponseResult.okResult();
    }

    /**
     * 菜单详情
     *
     * @param menuId
     * @return
     */
    @RequestMapping(value = "/system/menu/{id}", method = RequestMethod.GET)
    public ResponseResult detail(@PathVariable("id") Long menuId) {

        return ResponseResult.okResult(menuService.getById(menuId));
    }

    /**
     * 修改菜单
     *
     * @param menu
     * @return
     */
    @PutMapping("/system/menu")
    public ResponseResult editMenu(@RequestBody Menu menu) {

        // id 不能和父级id相同。
        if (menu.getId().equals(menu.getParentId())) {
            return ResponseResult.errorResult(500, "修改菜单'" + menu.getMenuName() + "'失败，上级菜单不能选择自己");
        }

        menuService.updateById(menu);

        return ResponseResult.okResult();

    }

    /**
     * 删除菜单
     */
    @DeleteMapping("/system/menu/{menuId}")
    public ResponseResult remove(@PathVariable("menuId") Long menuId) {
        if (menuService.hasChild(menuId)) {
            return ResponseResult.errorResult(500,"存在子菜单不允许删除");
        }
        menuService.removeById(menuId);
        return ResponseResult.okResult();
    }


    /**
     * 获取菜单下拉树列表
     */
    @GetMapping("/system/menu/treeselect")
    public ResponseResult treeselect() {
        //复用之前的selectMenuList方法。方法需要参数，参数可以用来进行条件查询，而这个方法不需要条件，所以直接new Menu()传入
        List<Menu> menus = menuService.selectMenuList(new MenuListDto());
        List<MenuTreeVo> options =  SystemConverter.buildMenuSelectTree(menus);
        return ResponseResult.okResult(options);
    }

    /**
     * 加载对应角色菜单列表树
     */
    @GetMapping(value = "/system/menu/roleMenuTreeselect/{roleId}")
    public ResponseResult roleMenuTreeSelect(@PathVariable("roleId") Long roleId) {
        List<Menu> menus = menuService.selectMenuList(new MenuListDto());
        List<Long> checkedKeys = menuService.selectMenuListByRoleId(roleId);
        List<MenuTreeVo> menuTreeVos = SystemConverter.buildMenuSelectTree(menus);
        RoleMenuTreeSelectVo vo = new RoleMenuTreeSelectVo(checkedKeys,menuTreeVos);
        return ResponseResult.okResult(vo);
    }
}
