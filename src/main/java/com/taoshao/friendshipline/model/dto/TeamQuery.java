package com.taoshao.friendshipline.model.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import com.taoshao.friendshipline.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * 队伍查询封装类
 *
 * @Author taoshao
 * @Date 2024/6/15
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class TeamQuery extends PageRequest {
    /**
     * id
     */
    private Long id;
    /**
     * id 列表
     */
    private List<Long> idList;


    /**
     * 搜索关键词
     */
    private String searchText;

    /**
     * 队伍名称
     */
    private String name;

    /**
     * 描述
     */
    private String description;

    /**
     * 最大人数
     */
    private Integer maxNum;


    /**
     * 用户id
     */
    private Long userId;

    /**
     * 0-公开，1-私有，2-加密
     */
    private Integer status;



    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
