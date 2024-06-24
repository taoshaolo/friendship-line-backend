package com.taoshao.friendshipline.service;

import com.taoshao.friendshipline.model.domain.Message;
import com.baomidou.mybatisplus.extension.service.IService;
import com.taoshao.friendshipline.model.vo.MessageVo;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 *
 */
public interface MessageService extends IService<Message> {

    /**
     * 获取当前用户的消息列表
     * @param request
     * @return
     */
    List<MessageVo> listMessages(HttpServletRequest request);

    /**
     * 根据 发送方id 和 接收方id 获取历史记录
     * @param fromUserId
     * @param toUserId
     * @return
     */
    List<MessageVo> getUserHistoryMessage(Long fromUserId, Long toUserId);


    /**
     * 根据 发送方id 和 房间id 获取历史记录
     * @param fromUserId
     * @param toRoomId
     * @return
     */
    List<MessageVo> getRoomHistoryMessage(Long fromUserId, Long toRoomId);
}
