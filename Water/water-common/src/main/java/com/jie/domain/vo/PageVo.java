package com.jie.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageVo {

    // 所有的数据
    private List rows;
    // 总记录条数
    private Long total;
}
