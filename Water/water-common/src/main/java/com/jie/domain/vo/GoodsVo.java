package com.jie.domain.vo;

import lombok.Data;

/**
 * @Author: 曾凡杰
 * @version: 1.0
 * @className GoodsVo
 * @Description: TODO
 * @since: 1.0
 * @DateTime: 2023/7/2 11:59
 **/
@Data
public class GoodsVo {
    private Long id;
    // 商品名称
    private String storeName;
    //商品价格
    private Double price;
    //市场价
    private Double otPrice;
    //商品描述
    private String description;
    //商品图片
    private String image;

    private String productId;
}
