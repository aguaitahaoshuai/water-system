package com.jie.enums;


public enum AppHttpCodeEnum {
    SUCCESS(200,"操作成功"),
    LOGIN_SUCCESS(200,"登录成功！"),
    LOGOUT_SUCCESS(200,"退出登录成功！"),
    NEED_LOGIN(401,"需要登录后操作"),
    NO_OPERATOR_AUTH(403,"无权限操作"),
    SYSTEM_ERROR(500,"出现错误"),
    USERNAME_EXIST(501,"用户名已存在"),
    PHONENUMBER_EXIST(502,"手机号已存在"),
    EMAIL_EXIST(503, "邮箱已存在"),
    REQUIRE_USERNAME(504, "必需填写用户名"),
    LOGIN_ERROR(505, "用户名或密码错误"),
    FILE_IS_NULL(506, "文件为空"),
    FILE_TYPE_ERROR(507, "类型错误"),
    FILE_SIZE_ERROR(508, "文件超过最大上载大小"),
    NICK_NAME_EXISTS(509, "昵称已存在"),
    REQUIRE_EMAIL(510, "必需填写邮箱"),
    REQUIRE_NICKNAME(511, "必需填写昵称"),
    REQUIRE_PASSWORD(512, "必需填写密码"),
    TAG_NAME_EXISTS(513,"标签已存在"),
    TAG_ID_IS_NULL(514,"删除标签的id不能是空！");



    int code;
    String msg;

    AppHttpCodeEnum(int code, String errorMessage){
        this.code = code;
        this.msg = errorMessage;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
