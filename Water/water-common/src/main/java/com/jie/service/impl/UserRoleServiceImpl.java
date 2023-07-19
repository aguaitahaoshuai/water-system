package com.jie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jie.domain.entity.UserRole;
import com.jie.mapper.UserRoleMapper;
import com.jie.service.UserRoleService;
import org.springframework.stereotype.Service;


@Service
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRole> implements UserRoleService {
}
