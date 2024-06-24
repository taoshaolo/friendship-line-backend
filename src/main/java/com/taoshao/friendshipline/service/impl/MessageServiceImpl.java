package com.taoshao.friendshipline.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.taoshao.friendshipline.model.domain.Message;
import com.taoshao.friendshipline.model.domain.Team;
import com.taoshao.friendshipline.model.domain.User;
import com.taoshao.friendshipline.model.domain.UserTeam;
import com.taoshao.friendshipline.model.vo.MessageVo;
import com.taoshao.friendshipline.service.MessageService;
import com.taoshao.friendshipline.mapper.MessageMapper;
import com.taoshao.friendshipline.service.TeamService;
import com.taoshao.friendshipline.service.UserService;
import com.taoshao.friendshipline.service.UserTeamService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 *
 */
@Service
public class MessageServiceImpl extends ServiceImpl<MessageMapper, Message>
        implements MessageService {

    @Resource
    private UserService userService;
    @Resource
    private TeamService teamService;
    @Resource
    private UserTeamService userTeamService;

    @Override
    public List<MessageVo> listMessages(HttpServletRequest request) {

        List<MessageVo> result = new ArrayList<>();
        // 获取当前用户
        User loginUser = userService.getLoginUser(request);
        Long userId = loginUser.getId();
        List<Message> messageList = this.query()
                .eq("sendUserId", loginUser.getId())
                .or()
                .eq("receiveUserId", loginUser.getId())
                .orderByDesc("sendTime")
                .list();
        List<UserTeam> userTeamList = userTeamService.query()
                .eq("userId", userId)
                .list();
        userTeamList.forEach(userTeam -> {
            Long teamId = userTeam.getTeamId();
            List<Message> roomMessageList = this.query()
                    .eq("receiveUserId", teamId)
                    .orderByAsc("sendTime")
                    .list();
        });
        messageList.forEach(message -> {
            Long receiveUserId = message.getReceiveUserId();
            Long sendUserId = message.getSendUserId();
            Long teamId = message.getTeamId();
            MessageVo messageVo = new MessageVo();
            BeanUtils.copyProperties(message, messageVo);
            // 判断接收方类型是私聊还是群聊，群聊则需要查询 聊天室信息（头像、名称等）
            Integer receiveType = message.getReceiveType();
            if (receiveType == 1) {
                Team team = teamService.getById(teamId);
                messageVo.setAvatarUrl(team.getTeamImage());
                messageVo.setUserName(team.getName());
                result.add(messageVo);
            } else {
                if (!sendUserId.equals(loginUser.getId())) {
                    // 通过发送方id拿到头像和姓名并赋值
                    User user = userService.getById(sendUserId);
                    messageVo.setAvatarUrl(user.getAvatarUrl());
                    messageVo.setUserName(user.getUsername());
                    result.add(messageVo);
                }

            }
        });
        return result;
    }

    @Override
    public List<MessageVo> getUserHistoryMessage(Long fromUserId, Long toUserId) {

        List<MessageVo> result = new ArrayList<>();
        List<Message> messageList = this.query()
                .and(i -> i.eq("teamId",0).eq("receiveUserId", toUserId).eq("sendUserId", fromUserId))
                .or(i -> i.and(j -> j.eq("teamId",0).eq("receiveUserId", fromUserId).eq("sendUserId", toUserId)))
                .orderByAsc("sendTime")
                .list();

        messageList.forEach(message -> {
            MessageVo messageVo = new MessageVo();
            BeanUtils.copyProperties(message, messageVo);
            Long sendUserId = messageVo.getSendUserId();
            // 通过发送方id查询头像和姓名
            User sendUserInfo = userService.getById(message.getSendUserId());
            messageVo.setUserName(sendUserInfo.getUsername());
            messageVo.setAvatarUrl(sendUserInfo.getAvatarUrl());
            result.add(messageVo);

        });
        return result;
    }

    @Override
    public List<MessageVo> getRoomHistoryMessage(Long fromUserId, Long toRoomId) {
        List<MessageVo> result = new ArrayList<>();
        // 查询房间号下的所有消息
        List<Message> messageList = this.query()
                .eq("teamId", toRoomId)
                .orderByAsc("sendTime")
                .list();

        messageList.forEach(message -> {
            MessageVo messageVo = new MessageVo();
            BeanUtils.copyProperties(message, messageVo);
            // 通过发送方id查询头像和姓名
            User sendUserInfo = userService.getById(message.getSendUserId());
            messageVo.setUserName(sendUserInfo.getUsername());
            messageVo.setAvatarUrl(sendUserInfo.getAvatarUrl());
            result.add(messageVo);
        });
        return result;
    }
}




