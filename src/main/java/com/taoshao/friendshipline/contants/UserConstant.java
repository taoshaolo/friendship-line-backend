package com.taoshao.friendshipline.contants;

/**
 * 用户常量
 *
 * @Author taoshao
 */
public interface UserConstant {

    /**
     * 用户登录态键
     */
    String USER_LOGIN_STATE = "userLoginState";

    //  ------- 权限 --------

    /**
     * 默认权限
     */
    int DEFAULT_ROLE = 0;

    

    /**
     * 管理员权限
     */
    int ADMIN_ROLE = 1;

    /**
     * 获取最匹配的用户
     */
    int MAX_MATCH_USER_NUM = 20;

}
