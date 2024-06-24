package com.taoshao.friendshipline.contants;

/**
 * redis常量
 * @Author taoshao
 */
public interface RedisConstant {

    /**
     * 推荐用户键
     */
    String RECOMMEND_USER = "friendshipline:user:recommend:";
    /**
     * 根据标签查找用户键
     */
    String SEARCH_TAGS = "friendshipline:user:searchtags:";
    /**
     * 获取最匹配的用户键
     */
    String MATCH_USER = "friendshipline:user:match:";

    /**
     * 缓存预热键
     */
    String PRO_CACHE_JOB = "friendshipline:procachejob:docache:lock";




}
