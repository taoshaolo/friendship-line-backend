package com.taoshao.friendshipline.controller;

import com.taoshao.friendshipline.common.BaseResponse;
import com.taoshao.friendshipline.common.ResultUtils;
import com.taoshao.friendshipline.model.vo.MessageVo;
import com.taoshao.friendshipline.service.MessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @Author taoshao
 * @Date 2024/6/18
 */
@RestController
@RequestMapping("/message")
@Slf4j
public class MessageController {

    @Resource
    private MessageService messageService;

    @GetMapping("/list")
    public BaseResponse<List<MessageVo>> listMessages(HttpServletRequest request){
        //查询队伍消息列表
        List<MessageVo> teamList = messageService.listMessages(request);
        return ResultUtils.success(teamList);
    }

    /**
     * 查询用户的消息记录
     *
     * @return List<MessageVo>
     */
    @GetMapping("/getUserHistoryMessage")
    public BaseResponse<List<MessageVo>> getUserHistoryMessage(@RequestParam("fromUserId") Long fromUserId,
                                                               @RequestParam("toUserId") Long toUserId, HttpServletRequest request) {
        List<MessageVo> result = messageService.getUserHistoryMessage(fromUserId, toUserId);
        return ResultUtils.success(result);
    }


    /**
     * 查询聊天室的消息列表
     *
     * @return List<MessageVo>
     */
    @GetMapping("/getRoomHistoryMessage")
    public BaseResponse<List<MessageVo>> getRoomHistoryMessage(@RequestParam("fromUserId") Long fromUserId,
                                                               @RequestParam("toRoomId") Long toRoomId, HttpServletRequest request) {
        List<MessageVo> result = messageService.getRoomHistoryMessage(fromUserId, toRoomId);
        return ResultUtils.success(result);
    }
}
