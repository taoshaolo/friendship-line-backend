package com.taoshao.friendshipline.common;

import lombok.Data;

import java.io.Serializable;

/**
 * 通用分页请求参数
 *
 * @Author taoshao
 * @Date 2024/6/15
 */
@Data
public class PageRequest implements Serializable {


    private static final long serialVersionUID = -4349408557730879056L;
    private int PageNum = 1;
    private int PageSize = 10;
}
