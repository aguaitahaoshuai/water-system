package com.jie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jie.domain.ResponseResult;
import com.jie.domain.entity.Water;

/**
 * (Water)表服务接口
 *
 * @author makejava
 * @since 2023-07-16 21:11:13
 */
public interface WaterService extends IService<Water> {

    ResponseResult pageList(Long pageNum, Long pageSize);
}

