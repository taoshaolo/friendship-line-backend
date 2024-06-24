package com.taoshao.friendshipline.once;
import java.util.Date;

import com.taoshao.friendshipline.mapper.UserMapper;
import com.taoshao.friendshipline.model.domain.User;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

import javax.annotation.Resource;

/**
 * @Author taoshao
 * @Date 2024/6/14
 */
@Component
public class InsertUsers {

    @Resource
    private UserMapper userMapper;

    /**
     * 批量插入用户
     */
//    @Scheduled(fixedDelay = 5000)
    public void doInsertUsers(){
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        final int INSERT_NUM = 1;
        for (int i = 0; i < INSERT_NUM; i++) {
            User user = new User();
            user.setUsername("小黑子");
            user.setUserAccount("xiaoheizi");
            user.setAvatarUrl("https://tse1-mm.cn.bing.net/th/id/OIP-C.LBVzOA0WvDhthZCpBMO3PgHaHZ?rs=1&pid=ImgDetMain");
            user.setGender(0);
            user.setUserPassword("eed24eb1a8b1d6ffd9683a77f9462294");
            user.setPhone("13456789001");
            user.setEmail("xiaoheizi@qq.com");
            user.setUserStatus(0);
            user.setUserRole(0);
            user.setTags("[\"Java\",\"男\",\"Python\"]");
            user.setProfile("我不是小黑子，我是ikun");
            userMapper.insert(user);

        }
        stopWatch.stop();
        System.out.println(stopWatch.getTotalTimeMillis());
    }
}
