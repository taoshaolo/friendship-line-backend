package com.taoshao.friendshipline.ws;


import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.taoshao.friendshipline.model.domain.Message;
import com.taoshao.friendshipline.model.domain.User;
import com.taoshao.friendshipline.model.enums.ReceiveTypeEnum;
import com.taoshao.friendshipline.service.MessageService;
import com.taoshao.friendshipline.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author taoshao
 */
@Slf4j
@ServerEndpoint("/chat_room/{fromUserId}/{toRoomId}")
@Component
public class ChatRoomWebSocket {

    private static ApplicationContext applicationContext;

    public static void setApplicationContext(ApplicationContext context) {
        applicationContext = context;
    }

    /**
     * 记录当前在线连接数
     * 外层：房间id
     * 内层：用户id
     */
    public static final Map<String, Map<String, Session>> sessionRoomMap = new ConcurrentHashMap<>();
    /**
     * 记录当前在线数
     */
    public static final HashMap<String, Session> sessionMap = new HashMap<>(10);

    private MessageService messageService;
    private UserService userService;


    /**
     * 连接建立成功调用的方法
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("fromUserId") String fromUserId, @PathParam("toRoomId") String toRoomId) {
        sessionMap.put(fromUserId, session);
        sessionRoomMap.put(toRoomId, sessionMap);
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
    public void onMessage(String message,
                          Session session,
                          @PathParam("fromUserId") String fromUserId,
                          @PathParam("toRoomId") String toRoomId) {
        log.info("服务端收到用户userId={}的消息:{}", fromUserId, message);
        /*
          注意： 用户ID 对 房间ID 关联发送消息，而不是对房间内的每个人发。
         */
        JSONObject obj = JSON.parseObject(message);
        String content = obj.getString("content");
        // 通过 toRoomId 查询所有用户
        Map<String, Session> stringSessionMap = sessionRoomMap.get(toRoomId);
        try {
            for (Session stringSession : stringSessionMap.values()) {
                // 排除自身，发送给其他人
                if (!stringSession.getId().equals(session.getId())) {
                    log.info("服务端给客户端[{}]发送消息{}", stringSession.getId(), message);
                    // 服务器端 再把消息组装一下，组装后的消息包含发送人和发送的文本内容
                    userService = applicationContext.getBean(UserService.class);
                    User user = userService.getById(Integer.valueOf(fromUserId));
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("sendUserId", fromUserId);
                    jsonObject.put("receiveUserId", toRoomId);
                    jsonObject.put("roomId", toRoomId);
                    jsonObject.put("avatarUrl", user.getAvatarUrl());
                    jsonObject.put("userName", user.getUsername());
                    jsonObject.put("content", content);
                    jsonObject.put("position", "left");
                    stringSession.getAsyncRemote().sendText(jsonObject.toString());
                }
            }
        } catch (Exception e) {
            log.error("服务端发送消息给客户端失败", e);
        }

        // 向消息表中插入数据
        Message messageEntity = new Message();
        messageEntity.setSendUserId(Long.valueOf(fromUserId));
        messageEntity.setReceiveUserId(Long.valueOf(toRoomId));
        messageEntity.setTeamId(Long.valueOf(toRoomId));
        messageEntity.setContent(content);
        messageEntity.setReceiveType(ReceiveTypeEnum.GROUP_CHAT.getCode());
        messageEntity.setSendType(ReceiveTypeEnum.GROUP_CHAT.getCode());
        messageService = applicationContext.getBean(MessageService.class);
        messageService.save(messageEntity);
    }


    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose(Session session, @PathParam("fromUserId") String fromUserId, @PathParam("toRoomId") String toRoomId) {
        Map<String, Session> roomSessionMap = sessionRoomMap.get(toRoomId);
        roomSessionMap.remove("fromRoomId");
        if (roomSessionMap.isEmpty()) {
            sessionRoomMap.remove(toRoomId);
        } else {
            sessionRoomMap.put(toRoomId, roomSessionMap);
        }
        log.info("有一连接关闭，移除userId={}的用户session, 当前在线人数为：{}", fromUserId, sessionRoomMap.get(toRoomId).size());
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

}
