package com.jie.domain.vo;

import lombok.Data;

import java.util.List;

/**
 * @Author: 曾凡杰
 * @version: 1.0
 * @className GoodsDetailVo
 * @Description: TODO
 * @since: 1.0
 * @DateTime: 2023/7/5 11:50
 **/
@Data
public class GoodsDetailVo {

    //商品 ID
    private Long id;
    //商品 productId
    private String productId;
    private Long categoryId;
    // 商品名称
    private String storeName;
    // 商品简介
    private String description;
    private String goodsDetail;
    // 商品相册
    private List<String> goodsPhotos;
    // 商品缩略图
    private String image;
    private Double otPrice;
    private Double price;
    // 库存
    private Long stock;

    // 是否收藏
    private String userCollect;

}
