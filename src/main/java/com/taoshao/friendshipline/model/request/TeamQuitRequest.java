package com.taoshao.friendshipline.model.request;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author taoshao
 * @Date 2024/6/15
 */
@Data
public class TeamQuitRequest implements Serializable {

    private static final long serialVersionUID = 2589755047526903531L;


    /**
     * 队伍id
     */
    private Long teamId;




}
