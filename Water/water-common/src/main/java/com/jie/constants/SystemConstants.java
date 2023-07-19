package com.jie.constants;


public class SystemConstants {

    /**
     * 管理员的用户id
     */
    public static final Long ADMIN_USER_ID = 1L;
    /**
     * 管理员的用户 role_key
     */
    public static final String ADMIN_ROLE_KEY = "admin";

    /**
     * 权限菜单是正常状态的
     */
    public static final String MENU_STATUS = "0";


    /**
     * 商品分类是正常状态的
     */
    public static final String STATUS_NORMAL = "0";


    /**
     * 存入redis中的 key的前缀，通过该前缀 和 用户id就可以获取用户信息. 前台
     * 例如： bloglogin:1
     */
    public static final String REDIS_USER_ID_PREFIX_SHOP = "shoplogin:";

    /**
     * 存入redis中的 key的前缀，通过该前缀 和 用户id就可以获取用户信息. 后台管理
     * 例如： adminlogin:1
     */
    public static final String REDIS_USER_ID_PREFIX_ADMIN = "adminlogin:";


    /**
     * 正常状态
     */
    public static final String NORMAL = "0";

    /**
     * 商品是否收藏 0 收藏 1 未收藏
     */
    public static final String IS_COLLECT = "0";

    /**
     *  商品是否收藏 0 收藏 1 未收藏
     */
    public static final String NOT_COLLECT = "1";

    /**
     *  商品未下单
     */
    public static final String UN_ORDER = "0";
}
