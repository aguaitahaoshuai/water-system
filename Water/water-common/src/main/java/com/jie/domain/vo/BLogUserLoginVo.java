package com.jie.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;



@Data
@AllArgsConstructor
@NoArgsConstructor
public class BLogUserLoginVo {
    private String token;
    // token超时时间
    private Long _expire;
    private UserInfoVo userInfo;
    private String msg;
    private Integer code;

}
