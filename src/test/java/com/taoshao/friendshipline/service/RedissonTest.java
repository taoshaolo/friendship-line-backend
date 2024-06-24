package com.taoshao.friendshipline.service;

/**
 * @Author taoshao
 * @Date 2024/6/15
 */

import org.junit.jupiter.api.Test;
import org.redisson.api.RList;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.taoshao.friendshipline.contants.RedisConstant.PRO_CACHE_JOB;

@SpringBootTest
public class RedissonTest {

    @Resource
    private RedissonClient redissonClient;

    @Test
    public void testRedisson() {

        List<String> list = new ArrayList<>();
        list.add("taoshao");
        list.get(0);
        System.out.println("list:" + list.get(0));
        list.remove(0);

        RList<Object> rList = redissonClient.getList("test");
//        rList.add("taoshao");
        System.out.println("rList:" + rList.get(0));
        rList.remove(0);

    }

    @Test
    public void testWatchDog(){
        RLock lock = redissonClient.getLock(PRO_CACHE_JOB);
        try {
            //只有一个线程能获取到锁
            if (lock.tryLock(0,-1, TimeUnit.MILLISECONDS)){
                Thread.sleep(300000);//要运行的方法
                System.out.println("getLock"+Thread.currentThread().getId());

            }
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
        }finally {
            //只能释放自己的锁
            if (lock.isHeldByCurrentThread()){
                System.out.println("unLock"+Thread.currentThread().getId());
                lock.unlock();
            }

        }
    }


}
