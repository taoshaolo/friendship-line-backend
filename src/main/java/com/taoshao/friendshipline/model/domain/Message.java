package com.taoshao.friendshipline.model.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 消息表
 * @TableName message
 */
@TableName(value ="message")
@Data
public class Message implements Serializable {
    /**
     * 消息id
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 发送方id 
     */
    private Long sendUserId;

    /**
     * 接受方id 
     */
    private Long receiveUserId;

    /**
     * 房间号（0-私聊，其他-房间id）
     */
    private Long teamId;

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