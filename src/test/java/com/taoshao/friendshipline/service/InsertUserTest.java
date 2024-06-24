package com.taoshao.friendshipline.service;

import com.taoshao.friendshipline.mapper.UserMapper;
import com.taoshao.friendshipline.model.domain.User;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.StopWatch;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * @Author taoshao
 * @Date 2024/6/14
 */
@SpringBootTest
public class InsertUserTest {

    @Resource
    private UserMapper userMapper;

    /**
     * 插入用户
     */
    @Test
    public void doInsertUsers() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        final int INSERT_NUM = 1000;//时间 3334
        for (int i = 0; i < INSERT_NUM; i++) {
            User user = new User();
            user.setUsername("小黑子");
            user.setUserAccount("xiaoheizi");
            user.setAvatarUrl("https://pics0.baidu.com/feed/a8014c086e061d956df73c056c2c6edd63d9caba.jpeg?token=bc040da811115a3e1ddf661976d7ecdb");
            user.setGender(0);
            user.setUserPassword("eed24eb1a8b1d6ffd9683a77f9462294");
            user.setPhone("13456789001");
            user.setEmail("xiaoheizi@qq.com");
            user.setUserStatus(0);
            user.setUserRole(0);
            user.setPlanetCode("111");
            user.setTags("[\"Java\",\"男\",\"Python\"]");
            user.setProfile("我不是小黑子，我是ikun");
            userMapper.insert(user);
        }
        stopWatch.stop();
        System.out.println(stopWatch.getTotalTimeMillis());
    }

    @Resource
    private UserService userService;

    /**
     * 分批插入用户
     */
    @Test
    public void doBatchInsertUsers() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
//        final int INSERT_NUM = 1000; //100个为一批，时间 1147
        final int INSERT_NUM = 100000;
        //共 10 万条 用时：
        // 1000个为一批，时间 26818
        // 10000个为一批，时间 27013
        // 50000个为一批，时间 26338
        ArrayList<User> userList = new ArrayList<>();
        for (int i = 0; i < INSERT_NUM; i++) {
            User user = new User();
            user.setUsername("小黑子");
            user.setUserAccount("xiaoheizi");
            user.setAvatarUrl("https://pics0.baidu.com/feed/a8014c086e061d956df73c056c2c6edd63d9caba.jpeg?token=bc040da811115a3e1ddf661976d7ecdb");
            user.setGender(0);
            user.setUserPassword("eed24eb1a8b1d6ffd9683a77f9462294");
            user.setPhone("13456789001");
            user.setEmail("xiaoheizi@qq.com");
            user.setUserStatus(0);
            user.setUserRole(0);
            user.setPlanetCode("111");
            user.setTags("[\"Java\",\"男\",\"Python\"]");
            user.setProfile("我不是小黑子，我是ikun");
            userList.add(user);//插入用户列表里
        }
//        userService.saveBatch(userList,100);//100个为一批
//        userService.saveBatch(userList,1000);//1000个为一批
//        userService.saveBatch(userList,10000);//10000个为一批
        userService.saveBatch(userList, 50000);//50000个为一批
        stopWatch.stop();
        System.out.println(stopWatch.getTotalTimeMillis());
    }


    // CPU 密集型：（复杂的算法、数据处理或计算密集型的操作）分配的核心线程数 = CPU - 1
    // IO 密集型：（读写文件、网络通信或数据库交互）分配的核心线程数可以大于 CPU 核数
    private ExecutorService executorService = new ThreadPoolExecutor(
            60 * 2, // 核心线程池中的线程数量最大为 60
            60 * 3, // 整个线程池中最多存在 1000 个线程
            5, // 空闲线程最大的存活时间为 10 分钟
            TimeUnit.MINUTES, // 时间单位为 分钟
            new ArrayBlockingQueue<>(20000), // 阻塞队列使用的是有界阻塞队列，容量为 10000
            Executors.defaultThreadFactory(), // 使用默认的线程工厂
            new ThreadPoolExecutor.AbortPolicy() // 任务的拒绝策略，默认的任务处理策略
    );

    /**
     * 并发批量插入用户
     */
    @Test
    public void doConcurrencyBatchInsertUsers() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        // 分成 10 组  每组 10000  共 10万条用户信息  用时 8261、6890
        // int batchSize = 10000;

        // 分成 20 组  每组 5000  共 10万条用户信息               用时 6626

        // 分成 20 组  每组 5000  共 10万条用户信息 （自定义线程池：参数[60,1000,10,分,10000]） 用时 6759

        // 分成 20 组  每组 5000  共 10万条用户信息 （自定义线程池：参数[60*2,60*3,50,分,20000]） 用时 6653
        // 没变快 原因：
        // 20 分任务，实际上只有 15 人干活， 有 5 个人干了 2 份活
        // int batchSize = 5000;

        // 分成 40 组  每组 2500  共 10万条用户信息               用时 6784
        // 分成 40 组  每组 2500  共 10万条用户信息 （自定义线程池） 用时 6952
        int batchSize = 5000;
        int j = 0;
        List<CompletableFuture<Void>> futureList = new ArrayList<>();
//        for (int i = 0; i < 10; i++) {
//        for (int i = 0; i < 20; i++) {
        for (int i = 0; i < 20; i++) {
            ArrayList<User> userList = new ArrayList<>();
            while (true) {//CPU计算
                j++;
                User user = new User();
                user.setUsername("小黑子");
                user.setUserAccount("xiaoheizi");
                user.setAvatarUrl("https://pics0.baidu.com/feed/a8014c086e061d956df73c056c2c6edd63d9caba.jpeg?token=bc040da811115a3e1ddf661976d7ecdb");
                user.setGender(0);
                user.setUserPassword("eed24eb1a8b1d6ffd9683a77f9462294");
                user.setPhone("13456789001");
                user.setEmail("xiaoheizi@qq.com");
                user.setUserStatus(0);
                user.setUserRole(0);
                user.setPlanetCode("111");
                user.setTags("[\"Java\",\"男\",\"Python\"]");
                user.setProfile("我不是小黑子，我是ikun");
                userList.add(user);//插入用户列表里
                if (j % batchSize == 0) {
                    break;
                }
            }
            //启动一个异步任务，
            CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
                System.out.println(Thread.currentThread().getName());
                userService.saveBatch(userList, batchSize);
            },executorService);// 自定义线程池
            // 不写第二个参数，默认使用的是公共的 ForkJoinPool 线程池
            futureList.add(future);
        }
        CompletableFuture.allOf(futureList.toArray(new CompletableFuture[]{}))
                .join();//如果还没有完成，join() 将阻塞当前线程，直到 CompletableFuture 完成。

        stopWatch.stop();
        System.out.println(stopWatch.getTotalTimeMillis());
    }

}
