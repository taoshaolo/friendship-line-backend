package com.taoshao.friendshipline.model.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 消息信息封装类
 */
@Data
public class MessageVo implements Serializable {
    /**
     * 消息id
     */
    private Long id;

    /**
     * 发送方id
     */
    private Long sendUserId;

    /**
     * 头像地址
     */
    private String avatarUrl;

    /**
     * 接收方id
     */
    private Long receiveUserId;
    /**
     * 队伍id
     */
    private Long teamId;


    /**
     * 名称
     */
    private String userName;

    /**
     * 发送内容
     */
    private String content;

    /**
     * 发送时间
     */
    private Date sendTime;

    /**
     * 接收方类型（0-私聊，1-群聊）
     */
    private Integer receiveType;

    /**
     * 发送方类型（0-私发，1-群发）
     */
    private Integer sendType;

    /**
     * 已读时间
     */
    private Date readTime;

    /**
     * 类型（批量发送）
     */
    private Integer type;

    /**
     * 撤销时间
     */
    private Date cancelTime;

    /**
     * 是否撤销（1-是 0-否）
     */
    private Integer isCancel;

    /**
     * 是否已读（1-是 0-否）
     */
    private Integer isRead;


    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
