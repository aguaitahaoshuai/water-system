package com.jie.domain.dto;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;


@Data
public class UserRegisterDto {

    @NotNull(message = "用户不能为空")
    @Length(min = 4,max = 10,message = "姓名长度错误，长度4-10！")
    private String userName;

    @Length(min = 4,max = 10,message = "昵称长度错误，长度4-10！")
    @NotNull(message = "昵称不能为空")
    private String nickName;

    @Email(message = "邮箱格式错误")
    @NotNull(message = "邮箱不能为空")
    private String email;

    @NotNull(message = "密码不能为空")
    @Length(min = 4,max = 10,message = "密码长度错误，长度4-10！")
    private String password;
}
