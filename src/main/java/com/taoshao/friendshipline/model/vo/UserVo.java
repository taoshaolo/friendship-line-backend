package com.taoshao.friendshipline.model.vo;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户包装类
 * 
 */

@Data
public class UserVo implements Serializable {
    /**
     * id
     */
    private Long id;

    /**
     * 用户昵称
     */
    private String username;

    /**
     * 账号
     */
    private String userAccount;

    /**
     * 用户头像
     */
    private String avatarUrl;

    /**
     * 性别
     */
    private Integer gender;


    /**
     * 电话
     */
    private String phone;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 状态 0 - 正常
     */
    private Integer userStatus;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     *
     */
    private Date updateTime;



    /**
     * 用户角色 0 - 普通用户 1 - 管理员
     */
    private Integer userRole;



    /**
     * 标签类别 json
     */
    private String tags;
    /**
     * 描述
     */
    private String profile;




    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}

