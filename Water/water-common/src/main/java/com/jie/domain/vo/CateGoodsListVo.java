package com.jie.domain.vo;

import lombok.Data;

import java.util.List;

/**
 * @Author: 曾凡杰
 * @version: 1.0
 * @className CateGoodsListVo
 * @Description: TODO
 * @since: 1.0
 * @DateTime: 2023/7/2 11:51
 **/
@Data
public class CateGoodsListVo {
    private Long id;
    private Long pid;
    private String name;
    private String icon;


    private List<GoodsVo> children;
}
