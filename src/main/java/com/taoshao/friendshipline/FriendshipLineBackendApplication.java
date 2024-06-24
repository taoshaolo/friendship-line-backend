package com.taoshao.friendshipline;



import com.taoshao.friendshipline.ws.ChatRoomWebSocket;
import com.taoshao.friendshipline.ws.UserChatWebSocket;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * 启动类
 * 
 */
@SpringBootApplication
@MapperScan("com.taoshao.friendshipline.mapper")
@EnableScheduling//开启定时任务
public class FriendshipLineBackendApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(FriendshipLineBackendApplication.class, args);
        UserChatWebSocket.setApplicationContext(context);
        ChatRoomWebSocket.setApplicationContext(context);
    }

}

