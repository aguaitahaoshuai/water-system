package com.jie.domain.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@EqualsAndHashCode
public class ExcelExpressVo {

    @ExcelProperty("物流公司名称")
    private String name;

    //描述
    @ExcelProperty("物流公司编码")
    private String code;

    //状态0:正常,1禁用
    @ExcelProperty("状态0:正常,1禁用")
    private String status;
}
