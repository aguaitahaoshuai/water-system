package com.jie.domain.vo;

import lombok.Data;

import java.util.List;


@Data
public class GoodsListVo {

    // id
    private Long id;
    // 商品id
    private String productId;
    //商品价格
    private Double price;
    //市场价格
    private Double otPrice;
    //商品描述
    private String description;

    //商品详情
    private String goodsDetail;
    //商品缩略图
    private String image;
    //0 表示商品审核通过，1 审核未通过 ， 2 未通过
    private String status;
    //排序
    private Integer sort;
    //商品库存
    private Long stock;
    //商品名称
    private String storeName;

//    //是否收藏  0 收藏 1 未收藏
//    private String userCollect;

    //是否人气商品
    private Long isHot;
    //是否新品
    private Long isNews;

    // 分类名称
    private String categoryName;
    // 商品相册
    private List<String> goodsPhotos;
}
