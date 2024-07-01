package com.taoshao.friendshipline.controller;

import com.taoshao.friendshipline.common.BaseResponse;
import com.taoshao.friendshipline.common.DeleteRequest;
import com.taoshao.friendshipline.common.ErrorCode;
import com.taoshao.friendshipline.common.ResultUtils;
import com.taoshao.friendshipline.exception.BusinessException;
import com.taoshao.friendshipline.model.domain.User;
import com.taoshao.friendshipline.model.vo.MessageVo;
import com.taoshao.friendshipline.service.MessageService;
import com.taoshao.friendshipline.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

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
    @Resource
    private UserService userService;

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

    /**
     * 删除消息
     * @param deleteRequest
     * @param request
     * @return
     */
    @PostMapping("/delete")
    public BaseResponse<Boolean> delete(@RequestBody DeleteRequest deleteRequest, HttpServletRequest request){
        if (deleteRequest == null || deleteRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        long id = deleteRequest.getId();
        User loginUser = userService.getLoginUser(request);
        boolean result = messageService.delete(id, loginUser);
        if (!result) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "删除失败");
        }
        return ResultUtils.success(true);

    }
}
