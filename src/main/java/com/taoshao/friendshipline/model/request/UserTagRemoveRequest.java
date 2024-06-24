package com.taoshao.friendshipline.model.request;

import lombok.Data;

import java.io.Serializable;
import java.util.List;


/**
 * 删除用户标签请求体
 *
 */
@Data
public class UserTagRemoveRequest implements Serializable {

    private static final long serialVersionUID = 6121458871274540023L;
    /**
     * 标签
     */
    private String tag;
    /**
     * 标签列表
     */
    private List<String> oldTags;
    /**
     * 用户ID
     */
    private Long id;

}
