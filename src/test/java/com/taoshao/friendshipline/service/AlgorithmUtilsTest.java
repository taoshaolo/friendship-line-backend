package com.taoshao.friendshipline.service;

import com.taoshao.friendshipline.utils.AlgorithmUtil;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

/**
 * 算法工具类测试
 *
 * @Author taoshao
 * @Date 2024/6/17
 */
@SpringBootTest
public class AlgorithmUtilsTest {

    @Test
    public void testCompareString(){
        String a = "涛少是小黑子";
        String b = "涛少不是小黑子";
        String c = "涛少是真爱粉不是小黑子";
        //1 a 通过 增删改 变成 b
        int score1 = AlgorithmUtil.minDistance(a, b);
        //5 a 通过 增删改 变成 c
        int score2 = AlgorithmUtil.minDistance(a, c);
        System.out.println(score1);
        System.out.println(score2);
    }

    @Test
    public void testCompareTags(){
        List<String> list1 = Arrays.asList("Java", "大一", "男");
        List<String> list2 = Arrays.asList("Java", "大二", "男");
        List<String> list3 = Arrays.asList("Python", "大二", "女");
        List<String> list4 = Arrays.asList( "大二", "女","Python");

        //1 list1 通过 增删改 变成 list2
        int score1 = AlgorithmUtil.minDistance(list1, list2);
        //3 list1 通过 增删改 变成 list3
        int score2 = AlgorithmUtil.minDistance(list1, list3);
        //
        int score3 = AlgorithmUtil.minDistance(list3, list4);
        System.out.println(score1);
        System.out.println(score2);
        System.out.println(score3);
    }

}
