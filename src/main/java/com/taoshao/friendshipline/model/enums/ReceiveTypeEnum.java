package com.taoshao.friendshipline.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 接收类型枚举类
 */
@Getter
@AllArgsConstructor
public enum ReceiveTypeEnum {

    /**
     *
     */
    PRIVATE_CHAT(0,"私聊"),
    GROUP_CHAT(1,"群聊");

    int code;
    String desc;

    public static String getDesById(Integer code) {
        ReceiveTypeEnum[] enums = values();
        for (ReceiveTypeEnum aEnum : enums) {
            if (aEnum.code == code) {
                return aEnum.desc;
            }
        }
        return "";
    }

    public static ReceiveTypeEnum getById(Integer code) {
        ReceiveTypeEnum[] enums = values();
        for (ReceiveTypeEnum aEnum : enums) {
            if (aEnum.code == code) {
                return aEnum;
            }
        }
        return null;
    }
}
