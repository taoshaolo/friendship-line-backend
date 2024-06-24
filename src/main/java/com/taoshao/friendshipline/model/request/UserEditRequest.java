package com.taoshao.friendshipline.model.request;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 用户更新信息请求体
 *
 */
@Data
public class UserEditRequest implements Serializable {
    /**
     * 用户ID
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
     * 头像
     */
    private String avatarUrl;

    /**
     * 性别（1：男 2：女）
     */
    private Integer gender;

    /**
     * 密码
     */
    private String userPassword;

    /**
     * 电话
     */
    private String phone;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 标签 json 列表
     */
    private List<String> tags;

    /**
     * 个人简介
     */
    private String profile;

    /**
     * 用户状态 (0：正常 )
     */
    private Integer userStatus;


    /**
     * 更新时间
     */
    private Date updateTime;


    /**
     * 用户角色(1：管理员 2：普通用户)
     */
    private Integer userRole;

    private static final long serialVersionUID = 1L;
}
