package com.taoshao.friendshipline.ws;


import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.taoshao.friendshipline.model.domain.Message;
import com.taoshao.friendshipline.model.domain.User;
import com.taoshao.friendshipline.service.MessageService;
import com.taoshao.friendshipline.service.UserService;
import com.taoshao.friendshipline.utils.ListUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author taoshao
 */
@Slf4j
@ServerEndpoint("/user_chat/{fromUserId}/{toUserId}")
@Component
public class UserChatWebSocket {


    private static ApplicationContext applicationContext;

    public static void setApplicationContext(ApplicationContext context) {
        applicationContext = context;
    }

    /**
     * 记录当前在线连接数
     */
    public static final Map<String, Session> sessionMap = new ConcurrentHashMap<>();

    private MessageService messageService;
    private UserService userService;


    /**
     * 连接建立成功调用的方法
     */
    @OnOpen
    public void onOpen(Session session,
                       @PathParam("fromUserId") String fromUserId,
                       @PathParam("toUserId") String toUserId) {
        // 该userId必须是自己的userId，建立一个连接
        sessionMap.put(fromUserId, session);

        log.info("有新用户加入，userId={}, 当前在线人数为：{}", fromUserId, sessionMap.size());

    }


    /**
     * 收到客户端消息后调用的方法
     * 后台收到客户端发送过来的消息
     * onMessage 是一个消息的中转站
     * 接受 浏览器端 socket.send 发送过来的 json数据
     *
     * @param message 客户端发送过来的消息
     */
    @OnMessage
    public void onMessage(String message, Session session,
                          @PathParam("fromUserId") String fromUserId,
                          @PathParam("toUserId") String toUserId) {
        log.info("服务端收到用户userId={}的消息:{}", fromUserId, message);
        Message msg = JSON.parseObject(message, Message.class);

        // {"to": "admin", "text": "聊天文本"}
        // 根据 to用户名来获取 session，再通过session发送消息文本
        Session toSession = sessionMap.get(String.valueOf(toUserId));

        //判断session是否正常
        if (toSession != null) {
            // 服务器端 再把消息组装一下，组装后的消息包含发送人和发送的文本内容
            userService = applicationContext.getBean(UserService.class);
            User user = userService.getById(Integer.valueOf(fromUserId));
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("sendUserId", fromUserId);
            jsonObject.put("receiveUserId", toUserId);
            jsonObject.put("avatarUrl", user.getAvatarUrl());
            jsonObject.put("userName", user.getUsername());
            jsonObject.put("content", msg.getContent());
            jsonObject.put("position", "left");
            try {
                toSession.getBasicRemote().sendText(jsonObject.toString());
            } catch (IOException e) {
                log.error("推送消息到指定用户发生错误：" + e.getMessage(), e);
                return;
            }
            log.info("该用户不在线，消息：{}", jsonObject);

        }else {
            log.info("发送失败，未找到用户userId={}的session", toUserId);
        }


        // 消息持久化
        Message messageEntity = new Message();
        messageEntity.setSendUserId(Long.valueOf(fromUserId));
        messageEntity.setReceiveUserId(Long.valueOf(toUserId));
        messageEntity.setContent(msg.getContent());
        messageEntity.setReceiveType(msg.getReceiveType());
        messageEntity.setSendType(msg.getSendType());
        messageService = applicationContext.getBean(MessageService.class);
        messageService.save(messageEntity);
    }


    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose(Session session, @PathParam("fromUserId") String fromUserId) {
        sessionMap.remove(fromUserId);
        log.info("有一连接关闭，移除userId={}的用户session, 当前在线人数为：{}", fromUserId, sessionMap.size());
    }

    @OnError
    public void onError(Session session, Throwable error) {
        log.error("发生错误");
        error.printStackTrace();
    }

    /**
     * 服务端发送消息给客户端
     */
    private void sendMessage(String message, Session toSession) {
        try {
            log.info("服务端给客户端[{}]发送消息{}", toSession.getId(), message);
            toSession.getBasicRemote().sendText(message);
        } catch (Exception e) {
            log.error("服务端发送消息给客户端失败", e);
        }
    }

    /**
     * 服务端发送消息给所有客户端
     */
    private void sendAllMessage(String message) {
        try {
            for (Session session : sessionMap.values()) {
                log.info("服务端给客户端[{}]发送消息{}", session.getId(), message);
                session.getBasicRemote().sendText(message);
            }
        } catch (Exception e) {
            log.error("服务端发送消息给客户端失败", e);
        }
    }

}
