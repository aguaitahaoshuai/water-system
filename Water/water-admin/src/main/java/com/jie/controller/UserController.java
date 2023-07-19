package com.jie.controller;

import com.jie.domain.ResponseResult;
import com.jie.domain.dto.ChangeUserStatusDto;
import com.jie.domain.entity.Role;
import com.jie.domain.entity.User;
import com.jie.domain.vo.UserInfoAndRoleIdsVo;
import com.jie.enums.AppHttpCodeEnum;
import com.jie.exception.SystemException;
import com.jie.service.RoleService;
import com.jie.service.UserService;
import com.jie.utils.SecurityUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;


@RequestMapping("/system/user")
@RestController
public class UserController {

    @Resource
    private UserService userService;
    @Resource
    private RoleService roleService;

    /**
     * 获取用户列表
     */
    @GetMapping("/list")
    public ResponseResult list(User user, Integer pageNum, Integer pageSize) {
        return userService.selectUserPage(user,pageNum,pageSize);
    }

    /**
     * 注册用户
     */
    @PostMapping
    public ResponseResult add(@RequestBody User user) {
        // 用户名不能为null
        if(!StringUtils.hasText(user.getUserName())){
            throw new SystemException(AppHttpCodeEnum.REQUIRE_USERNAME);
        }
        // 用户名不能重复
        if (!userService.checkUserNameUnique(user.getUserName())){
            throw new SystemException(AppHttpCodeEnum.USERNAME_EXIST);
        }
        // 手机号不能重复
        if (!userService.checkPhoneUnique(user)){
            throw new SystemException(AppHttpCodeEnum.PHONENUMBER_EXIST);
        }
        // 邮箱不能重复
        if (!userService.checkEmailUnique(user)){
            throw new SystemException(AppHttpCodeEnum.EMAIL_EXIST);
        }
        // 添加用户
        return userService.addUser(user);
    }

    /**
     * 根据用户编号获取详细信息
     */
    @GetMapping(value = { "/{userId}" })
    public ResponseResult getUserInfoAndRoleIds(@PathVariable(value = "userId") Long userId)
    {
        List<Role> roles = roleService.selectRoleAll();
        User user = userService.getById(userId);
        //当前用户所具有的角色id列表
        List<Long> roleIds = roleService.selectRoleIdByUserId(userId);

        UserInfoAndRoleIdsVo vo = new UserInfoAndRoleIdsVo(user,roles,roleIds);
        return ResponseResult.okResult(vo);
    }

    /**
     * 修改用户
     */
    @PutMapping
    public ResponseResult edit(@RequestBody User user) {
        userService.updateUser(user);
        return ResponseResult.okResult();
    }

    /**
     * 删除用户
     */
    @DeleteMapping("/{userIds}")
    public ResponseResult remove(@PathVariable List<Long> userIds) {
        if(userIds.contains(SecurityUtils.getUserId())){
            return ResponseResult.errorResult(500,"不能删除当前你正在使用的用户");
        }
        userService.removeByIds(userIds);
        return ResponseResult.okResult();
    }

    @PutMapping("/changeStatus")
    public ResponseResult changeStatus(@RequestBody ChangeUserStatusDto changeStatus){

        User user = new User();
        user.setId(changeStatus.getUserId());
        user.setStatus(changeStatus.getStatus());

        return ResponseResult.okResult(userService.updateById(user));
    }
}
