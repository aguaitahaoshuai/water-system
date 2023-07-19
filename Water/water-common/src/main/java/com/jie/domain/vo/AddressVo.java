package com.jie.domain.vo;

import lombok.Data;

/**
 * @Author: 曾凡杰
 * @version: 1.0
 * @className AddressVo
 * @Description: TODO
 * @since: 1.0
 * @DateTime: 2023/7/6 15:54
 **/
@Data
public class AddressVo {
    //主键，用于唯一标识每个地址。@TableId
    private Integer id;
    //用户ID，与其他表进行关联。
    private Long uid;
    //真实姓名
    private String realName;
    //手机号码
    private String phone;
    //province
    private String province;
    //城市
    private String city;
    //区县
    private String district;
    //详细地址信息
    private String detail;
    //邮政编码（可为空）
    private String postcode;
    // 是否默认
    private String isDefault;
}
