package com.jie.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserLoginVo {

    private String token;
    // token超时时间
//    private Long _expire;
//    private Integer code;
//    private String msg;

}
