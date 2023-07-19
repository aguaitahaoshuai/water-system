package com.jie.domain.vo;

import lombok.Data;

/**
 * @Author: 曾凡杰
 * @version: 1.0
 * @className CartListVo
 * @Description: TODO
 * @since: 1.0
 * @DateTime: 2023/7/6 1:30
 **/
@Data
public class CartListVo {

    private Long id;
    // 商品id
    private String productId;
    //商品价格
    private Double price;
    //市场价格
    private Double otPrice;
    //商品描述
    private String description;
    //商品缩略图
    private String image;
    //商品名称
    private String storeName;
    // 购物车数量
    private Integer cartNum;

}
