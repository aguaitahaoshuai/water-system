package com.jie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jie.domain.ResponseResult;
import com.jie.domain.dto.UserInfoDto;
import com.jie.domain.dto.UserRegisterDto;
import com.jie.domain.entity.User;
import com.jie.domain.entity.UserRole;
import com.jie.domain.vo.PageVo;
import com.jie.domain.vo.UserInfoVo;
import com.jie.domain.vo.UserVo;
import com.jie.enums.AppHttpCodeEnum;
import com.jie.exception.SystemException;
import com.jie.mapper.UserMapper;
import com.jie.service.UserRoleService;
import com.jie.service.UserService;
import com.jie.utils.BeanCopyUtils;
import com.jie.utils.SecurityUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Resource
    private PasswordEncoder passwordEncoder;



    @Override
    public ResponseResult getUserInfo() {

        Long userId = SecurityUtils.getUserId();


        User user = getById(userId);
        // 转换vo
        UserInfoVo userInfoVo = BeanCopyUtils.copyBean(user, UserInfoVo.class);
        return ResponseResult.okResult(userInfoVo);
    }

    @Override
    public ResponseResult updateUser(UserInfoDto userInfo) {

        // 将dto转换实体类
        User user = BeanCopyUtils.copyBean(userInfo, User.class);

        // 不允许更新密码
        user.setPassword(null);
//        // 用户昵称、邮箱不能为重复
//        List<User> userList = list();
//        userList.forEach(u -> {
//            if(u.getNickName().equals(user.getNickName())){
//                throw new SystemException(AppHttpCodeEnum.NICK_NAME_EXISTS);
//            }
//            if(u.getEmail().equals(user.getEmail())){
//                throw new SystemException(AppHttpCodeEnum.EMAIL_EXIST);
//            }
//        });

        boolean update = updateById(user);

        if (update) {
            return ResponseResult.okResult();
        }

        return ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR);
    }

    @Override
    public ResponseResult register(UserRegisterDto userRegisterDto) {

        // 判断用户注册的信息不能为null
//        if (!StringUtils.hasText(userRegisterDto.getUserName())){
//            // 用户名不能为空
//            throw new SystemException(AppHttpCodeEnum.REQUIRE_USERNAME);
//        }
//        if (!StringUtils.hasText(userRegisterDto.getNickName())){
//            // 昵称不能为空
//            throw new SystemException(AppHttpCodeEnum.REQUIRE_NICKNAME);
//        }
//        if (!StringUtils.hasText(userRegisterDto.getEmail())){
//            // 邮箱不能为空
//            throw new SystemException(AppHttpCodeEnum.REQUIRE_EMAIL);
//        }
//        if (!StringUtils.hasText(userRegisterDto.getPassword())){
//            // 密码不能为空
//            throw new SystemException(AppHttpCodeEnum.REQUIRE_PASSWORD);
//        }

        // 用户名重复
        if (userNameExists(userRegisterDto)){
            throw new SystemException(AppHttpCodeEnum.USERNAME_EXIST);
        }
        // 昵称重复
        if (nickNameExists(userRegisterDto)){
            throw new SystemException(AppHttpCodeEnum.NICK_NAME_EXISTS);
        }
        // 邮箱重复
        if (emailExists(userRegisterDto)){
            throw new SystemException(AppHttpCodeEnum.EMAIL_EXIST);
        }

        // 对密码进行加密
        String passwordEncode = passwordEncoder.encode(userRegisterDto.getPassword());

        // 转换User对象
        User user = BeanCopyUtils.copyBean(userRegisterDto, User.class);

        user.setPassword(passwordEncode);

        // 执行插入数据库操作。
        if (!save(user)){
            return ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR);
        }

        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult selectUserPage(User user, Integer pageNum, Integer pageSize) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper();

        queryWrapper.like(StringUtils.hasText(user.getUserName()),User::getUserName,user.getUserName());
        queryWrapper.eq(StringUtils.hasText(user.getStatus()),User::getStatus,user.getStatus());
        queryWrapper.eq(StringUtils.hasText(user.getPhonenumber()),User::getPhonenumber,user.getPhonenumber());

        Page<User> page = new Page<>();
        page.setCurrent(pageNum);
        page.setSize(pageSize);
        page(page,queryWrapper);

        //转换成VO
        List<User> users = page.getRecords();
        List<UserVo> userVoList = users.stream()
                .map(u -> BeanCopyUtils.copyBean(u, UserVo.class))
                .collect(Collectors.toList());
        PageVo pageVo = new PageVo();
        pageVo.setTotal(page.getTotal());
        pageVo.setRows(userVoList);
        return ResponseResult.okResult(pageVo);
    }

    @Override
    public boolean checkUserNameUnique(String userName) {
        return count(Wrappers.<User>lambdaQuery().eq(User::getUserName,userName))==0;
    }

    @Override
    public boolean checkPhoneUnique(User user) {
        return count(Wrappers.<User>lambdaQuery().eq(User::getPhonenumber,user.getPhonenumber()))==0;
    }

    @Override
    public boolean checkEmailUnique(User user) {
        return count(Wrappers.<User>lambdaQuery().eq(User::getEmail,user.getEmail()))==0;
    }

    @Override
    @Transactional
    public ResponseResult addUser(User user) {
        //密码加密处理
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        save(user);

        if(user.getRoleIds()!=null&&user.getRoleIds().length>0){
            insertUserRole(user);
        }


        return ResponseResult.okResult();
    }

    @Override
    @Transactional
    public void updateUser(User user) {
        // 删除用户与角色关联
        LambdaQueryWrapper<UserRole> userRoleUpdateWrapper = new LambdaQueryWrapper<>();
        userRoleUpdateWrapper.eq(UserRole::getUserId,user.getId());
        userRoleService.remove(userRoleUpdateWrapper);

        // 新增用户与角色管理
        insertUserRole(user);
        // 更新用户信息
        updateById(user);
    }
    @Resource
    private UserRoleService userRoleService;

    private void insertUserRole(User user) {
        List<UserRole> sysUserRoles = Arrays.stream(user.getRoleIds())
                .map(roleId -> new UserRole(user.getId(), roleId)).collect(Collectors.toList());
        userRoleService.saveBatch(sysUserRoles);
    }

    /**
     * 用户名不能重复
     * @param userRegisterDto
     * @return true 存在
     */
    private boolean userNameExists(UserRegisterDto userRegisterDto){

        // 判断用户名不能重复
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User ::getUserName,userRegisterDto.getUserName() );

        return  count(queryWrapper) > 0;
    }
    /**
     * 昵称不能重复
     * @param userRegisterDto
     * @return true 存在
     */
    private boolean nickNameExists(UserRegisterDto userRegisterDto){

        // 判断昵称不能重复
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User ::getNickName,userRegisterDto.getNickName() );

        return  count(queryWrapper) > 0;
    }
    /**
     * 邮箱不能重复
     * @param userRegisterDto
     * @return true 存在
     */
    private boolean emailExists(UserRegisterDto userRegisterDto){

        // 判断邮箱不能重复
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User ::getEmail,userRegisterDto.getEmail() );

        return  count(queryWrapper) > 0;
    }


}
