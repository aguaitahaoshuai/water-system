package com.jie.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.jie.domain.ResponseResult;
import com.jie.domain.entity.Water;
import com.jie.service.WaterService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @Author: 曾凡杰
 * @version: 1.0
 * @className WaterController
 * @Description: TODO
 * @since: 1.0
 * @DateTime: 2023/7/16 21:12
 **/
@RestController
@RequestMapping("/content/water")
public class WaterController {

    @Resource
    private WaterService waterService;

    /**
     * 分页查询列表数据
     * @param pageNum
     * @param pageSize
     * @return
     */
    @GetMapping("/list")
    public ResponseResult list(Long pageNum, Long pageSize){

        return waterService.pageList(pageNum,pageSize);
    }

    /**
     * 详情
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public ResponseResult detail(@PathVariable Integer id){

        return ResponseResult.okResult(waterService.getById(id));
    }
    /**
     * 添加
     * @param
     * @return
     */
    @PostMapping
    public ResponseResult add(@RequestBody Water water){
        waterService.save(water);
        return ResponseResult.okResult();
    }

    /**
     * 修改
     * @param
     * @return
     */
    @PutMapping
    public ResponseResult modify(@RequestBody Water water){
        waterService.updateById(water);
        return ResponseResult.okResult();
    }

    /**
     * 删除
     * @param
     * @return
     */
    @DeleteMapping("/{id}")
    public ResponseResult delete(@PathVariable Integer id){
        waterService.removeById(id);
        return ResponseResult.okResult();
    }

    @GetMapping("/new")
    public ResponseResult newData(){

        Water water = waterService.getOne(Wrappers.<Water>lambdaQuery().orderByDesc(Water::getCreateTime), false);

        return ResponseResult.okResult(water);
    }





}
