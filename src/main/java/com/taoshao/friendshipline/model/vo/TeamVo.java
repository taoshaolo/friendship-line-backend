package com.taoshao.friendshipline.model.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 队伍用户信息封装类
 *
 * @Author taoshao
 * @Date 2024/6/16
 */
@Data
public class TeamVo implements Serializable {
    private static final long serialVersionUID = 5722701608174907409L;
    /**
     * id
     */
    private Long id;

    /**
     * 队伍名称
     */
    private String name;
    /**
     * 队伍照片
     */
    private String teamImage;

    /**
     * 描述
     */
    private String description;

    /**
     * 最大人数
     */
    private Integer maxNum;
    /**
     * 已加入的用户数
     */
    private Integer joinNum;


    /**
     * 过期时间
     */
    private Date expireTime;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 0-公开，1-私有，2-加密
     */
    private Integer status;

    /**
     * 创新时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 创建人
     */
    private String username;

    /**
     * 用户是否已加入
     */
    private Boolean hasJoin;

}
