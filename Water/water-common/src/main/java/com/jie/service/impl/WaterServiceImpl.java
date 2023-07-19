package com.jie.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jie.domain.ResponseResult;
import com.jie.domain.entity.Water;
import com.jie.domain.vo.PageVo;
import com.jie.mapper.WaterMapper;
import com.jie.service.WaterService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * (Water)表服务实现类
 *
 * @author makejava
 * @since 2023-07-16 21:11:13
 */
@Service("waterService")
public class WaterServiceImpl extends ServiceImpl<WaterMapper, Water> implements WaterService {

    @Override
    public ResponseResult pageList(Long pageNum, Long pageSize) {
         if (pageSize.equals(100L)){
             return  ResponseResult.okResult(new PageVo(getPageLastNum(pageNum,pageSize),null));
        }

        Page<Water> waterPage = new Page<>();
        waterPage.setCurrent(pageNum);
        waterPage.setSize(pageSize);
        page(waterPage, Wrappers.<Water>lambdaQuery().orderByAsc(Water::getId));


        return ResponseResult.okResult(new PageVo(waterPage.getRecords(), waterPage.getTotal()));
    }


    // 按照id进行排序。第一页就是最新的数据。然后再排序
    public List<Water> getPageLastNum(Long pageNum, Long pageSize){
        Page<Water> waterPage = new Page<>();
        waterPage.setCurrent(pageNum);
        waterPage.setSize(pageSize);

        page(waterPage, Wrappers.<Water>lambdaQuery().orderByDesc(Water::getId));

        List<Water> list = waterPage.getRecords().stream().sorted((a, b) -> a.getId() - b.getId()).collect(Collectors.toList());
        return list;
    }
}


