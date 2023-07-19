package com.jie.controller;

import com.jie.domain.ResponseResult;
import com.jie.domain.entity.User;
import com.jie.domain.vo.MenuVo;
import com.jie.domain.vo.RoutersVo;
import com.jie.service.MenuService;
import com.jie.utils.SecurityUtils;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;


@RestController
public class RoutersController {

    @Resource
    private  MenuService menuService;


    @RequestMapping("/getRouters")
    public ResponseResult getRouters(){

//        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        User user = SecurityUtils.getLoginUser().getUser();

        List<MenuVo> menus = menuService.selectRouterMenuTreeByUserId(user.getId());

        return ResponseResult.okResult(new RoutersVo(menus));
    }
}
