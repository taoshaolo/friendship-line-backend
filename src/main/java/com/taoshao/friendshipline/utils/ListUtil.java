package com.taoshao.friendshipline.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class ListUtil {
    public static <T> List<T> toList(T object) {
        // 假设object是一个集合类型的实例
        if (object instanceof Collection) {
            return new ArrayList<>((Collection<T>) object);
        }
        // 如果不是集合类型，创建一个只包含这个对象的列表
        return Collections.singletonList(object);
    }
}