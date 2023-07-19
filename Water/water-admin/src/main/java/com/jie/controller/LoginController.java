package com.jie.controller;

import com.jie.domain.ResponseResult;
import com.jie.domain.dto.UserLoginDto;
import com.jie.domain.vo.AdminUserInfoVo;
import com.jie.domain.entity.User;
import com.jie.domain.vo.UserInfoVo;
import com.jie.enums.AppHttpCodeEnum;
import com.jie.exception.SystemException;
import com.jie.service.SystemLoginService;
import com.jie.service.MenuService;
import com.jie.service.RoleService;
import com.jie.service.UserService;
import com.jie.utils.BeanCopyUtils;

import com.jie.utils.SecurityUtils;
import io.jsonwebtoken.lang.Strings;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;


@RestController
public class LoginController {

    @Resource
    private SystemLoginService systemLoginService;

    @Resource
    private RoleService roleService;
    @Resource
    private MenuService menuService;

    @Resource
    private UserService userService;

    @RequestMapping(value = "/user/login", method = RequestMethod.POST)
    public ResponseResult login(@RequestBody UserLoginDto userLogin) {

        if (!Strings.hasText(userLogin.getUserName())) {
            throw new SystemException(AppHttpCodeEnum.REQUIRE_USERNAME);
        }
        if (!Strings.hasText(userLogin.getPassword())) {
            throw new SystemException(AppHttpCodeEnum.REQUIRE_PASSWORD);
        }


        return systemLoginService.login(BeanCopyUtils.copyBean(userLogin, User.class));
    }


    @RequestMapping(value = "/getInfo", method = RequestMethod.GET)
    public ResponseResult<AdminUserInfoVo> getInfo() {



        // 如果用户id为1代表管理员，roles 中只需要有admin，permissions中需要有所有菜单类型为C或者F的，状态为正常的，未被删除的权限。
        // TODO 1、 获取当前登陆的系统用户
        User user = SecurityUtils.getLoginUser().getUser();

//        // TODO 2、 获取权限 permissions中需要有所有菜单类型为C或者F的，状态为正常的，未被删除的权限
        List<String> permissions = menuService.selectPermissionsByUserId(user.getId());
//
//
//        // TODO 3、 roles 中只需要有admin 查询角色所拥有的权限
        List<String> roles = roleService.selectRoleKeyByUserId(user.getId());
//
//        // 封装vo
        UserInfoVo userInfoVo = BeanCopyUtils.copyBean(user, UserInfoVo.class);

        AdminUserInfoVo adminUserInfoVo = new AdminUserInfoVo(permissions, roles, userInfoVo);


        return ResponseResult.okResult(adminUserInfoVo);
    }


    @RequestMapping(value = "/user/logout", method = RequestMethod.POST)
    public ResponseResult logout() {

        return systemLoginService.logout();
    }

}
