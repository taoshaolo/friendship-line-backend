package com.taoshao.friendshipline.job;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.taoshao.friendshipline.model.domain.User;
import com.taoshao.friendshipline.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.taoshao.friendshipline.contants.RedisConstant.PRO_CACHE_JOB;
import static com.taoshao.friendshipline.contants.RedisConstant.RECOMMEND_USER;

/**
 * 缓存预热任务
 *
 * @Author taoshao
 * @Date 2024/6/15
 */
@Component
@Slf4j
public class PreCacheJob {

    @Resource
    private UserService userService;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Resource
    private RedissonClient redissonClient;

    //重点用户
    private List<Long> mainUserList = Arrays.asList(1L);

    //每日执行，预热推荐用户
    @Scheduled(cron = "0 22 18 * * *")
    public void doCacheReCommend() {
        RLock lock = redissonClient.getLock(PRO_CACHE_JOB);
        try {
            // 只有一个线程能获取锁
            if (lock.tryLock(0, -1, TimeUnit.MILLISECONDS)) {
                System.out.println("getLock:"+ Thread.currentThread().getId());
                for (Long userId : mainUserList) {
                    QueryWrapper<User> queryWrapper = new QueryWrapper<>();
                    Page<User> userPage = userService.page(new Page<>(1, 8), queryWrapper);
                    String redisKey = RECOMMEND_USER + userId + ":" + 1;
                    ValueOperations<String, Object> valueOperations = redisTemplate.opsForValue();
                    //存入 redis
                    try {
                        valueOperations.set(redisKey, userPage, 1, TimeUnit.MINUTES);
                    } catch (Exception e) {
                        log.error("redis set key error", e);
                    }
                }
            }
        } catch (InterruptedException e) {
            log.error("doCacheReCommend error", e);
        } finally {
            //只能释放自己的锁
            if (lock.isHeldByCurrentThread()) {
                System.out.println("unlock:"+ Thread.currentThread().getId());
                lock.unlock();
            }
        }


    }
}
