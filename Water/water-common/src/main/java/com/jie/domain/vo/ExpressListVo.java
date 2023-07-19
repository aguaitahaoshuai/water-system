package com.jie.domain.vo;

import lombok.Data;


@Data
public class ExpressListVo {

    //物流公司的主键id
    private Long id;
    // 物流公司名称
    private String name;

    // 物流公司编码
    private String code;
    // 状态
    private String status;
}
