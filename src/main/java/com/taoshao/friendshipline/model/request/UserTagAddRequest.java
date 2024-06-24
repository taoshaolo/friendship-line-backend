package com.taoshao.friendshipline.model.request;

import lombok.Data;

import java.io.Serializable;


/**
 * 添加用户标签请求体
 *
 */
@Data
public class UserTagAddRequest implements Serializable {

    private static final long serialVersionUID = 6121458871274540023L;
    /**
     * 标签
     */
    private String tag;
    /**
     * 用户ID
     */
    private Long id;

}
