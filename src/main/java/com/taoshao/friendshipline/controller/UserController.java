package com.taoshao.friendshipline.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.taoshao.friendshipline.contants.UserConstant;
import com.taoshao.friendshipline.model.request.*;
import com.taoshao.friendshipline.service.UserService;
import com.taoshao.friendshipline.common.BaseResponse;
import com.taoshao.friendshipline.common.ErrorCode;
import com.taoshao.friendshipline.common.ResultUtils;
import com.taoshao.friendshipline.exception.BusinessException;
import com.taoshao.friendshipline.model.domain.User;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static com.taoshao.friendshipline.contants.RedisConstant.*;
import static com.taoshao.friendshipline.contants.UserConstant.MAX_MATCH_USER_NUM;

/**
 * 用户接口
 */
@RestController
@RequestMapping("/user")
//@CrossOrigin(origins = {"http://localhost:5173"})
@Slf4j
public class UserController {

    @Resource
    private UserService userService;
    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 用户注册
     *
     * @param userRegisterRequest
     * @return
     */
    @PostMapping("/register")
    public BaseResponse<Long> userRegister(@RequestBody UserRegisterRequest userRegisterRequest) {
        // 校验
        if (userRegisterRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        String userAccount = userRegisterRequest.getUserAccount();
        String userPassword = userRegisterRequest.getUserPassword();
        String checkPassword = userRegisterRequest.getCheckPassword();

        if (StringUtils.isAnyBlank(userAccount, userPassword, checkPassword)) {
            return null;
        }
        long result = userService.userRegister(userAccount, userPassword, checkPassword);
        return ResultUtils.success(result);
    }

    /**
     * 用户登录
     *
     * @param userLoginRequest
     * @param request
     * @return
     */
    @PostMapping("/login")
    public BaseResponse<User> userLogin(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest request) {
        if (userLoginRequest == null) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR);
        }
        String userAccount = userLoginRequest.getUserAccount();
        String userPassword = userLoginRequest.getUserPassword();
        if (StringUtils.isAnyBlank(userAccount, userPassword)) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR);
        }
        User user = userService.userLogin(userAccount, userPassword, request);
        return ResultUtils.success(user);
    }

    /**
     * 用户注销
     *
     * @param request
     * @return
     */
    @PostMapping("/logout")
    public BaseResponse<Integer> userLogout(HttpServletRequest request) {
        if (request == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        int result = userService.userLogout(request);
        return ResultUtils.success(result);
    }

    /**
     * 获取当前用户
     *
     * @param request
     * @return
     */
    @GetMapping("/current")
    public BaseResponse<User> getCurrentUser(HttpServletRequest request) {
        Object userObj = request.getSession().getAttribute(UserConstant.USER_LOGIN_STATE);
        User currentUser = (User) userObj;
        if (currentUser == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN);
        }
        long userId = currentUser.getId();
        // TODO 校验用户是否合法
        User user = userService.getById(userId);
        User safetyUser = userService.getSafetyUser(user);
        return ResultUtils.success(safetyUser);
    }

    /**
     * 更新用户信息
     *
     * @param user
     * @param request
     * @return
     */
    @PostMapping("/update")
    public BaseResponse<Integer> update(@RequestBody UserEditRequest user, HttpServletRequest request) {
        //1.检验参数是否为空
        if (user == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        //鉴权
        User loginUser = userService.getLoginUser(request);

        int result = userService.updateUser(user, loginUser);
        return ResultUtils.success(result);
    }


    /**
     * 添加用户标签
     *
     * @param userTagAddRequest
     * @param request
     * @return
     */
    @PostMapping("/tag/add")
    public BaseResponse<Boolean> addTag(@RequestBody UserTagAddRequest userTagAddRequest, HttpServletRequest request) {
        //1.检验参数
        if (userTagAddRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        //2.校验权限
        User loginUser = userService.getLoginUser(request);
        if (loginUser == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN);
        }
        userTagAddRequest.setId(loginUser.getId());
        //3.添加
        Boolean result = userService.addTag(userTagAddRequest);
        return ResultUtils.success(result);
    }

    @PostMapping
    public BaseResponse<Boolean> removeTag(@RequestBody UserTagRemoveRequest userTagRemoveRequest, HttpServletRequest request) {
        if (userTagRemoveRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // 2. 校验权限
        User loginUser = userService.getLoginUser(request);
        if (loginUser == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN);
        }
        // 3. 触发更新
        Boolean result = userService.removeTag(userTagRemoveRequest);
        return ResultUtils.success(result);
    }


    /**
     * 搜索用户
     *
     * @param username
     * @param request
     * @return
     */
    @GetMapping("/search")
    public BaseResponse<List<User>> searchUsers(String username, HttpServletRequest request) {
        if (!userService.isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH, "缺少管理员权限");
        }
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(username)) {
            queryWrapper.like("username", username);
        }
        List<User> userList = userService.list(queryWrapper);
        List<User> list = userList.stream().map(user -> userService.getSafetyUser(user)).collect(Collectors.toList());
        return ResultUtils.success(list);
    }

    /**
     * 主页推荐用户
     *
     * @param pageNum
     * @param pageSize
     * @param request
     * @return
     */
    @GetMapping("/recommend")
    public BaseResponse<Page<User>> recommendUsers(int pageNum, int pageSize, HttpServletRequest request) {
//        User loginUser = userService.getLoginUser(request);
//        Long userId = loginUser.getId();
        //有缓存，就读缓存
        String redisKey = RECOMMEND_USER + pageNum;
//        String redisKey = RECOMMEND_USER + userId + ":" + pageNum;
        ValueOperations<String, Object> valueOperations = redisTemplate.opsForValue();
        Page<User> userPage = (Page<User>) valueOperations.get(redisKey);
        if (userPage != null) {
            return ResultUtils.success(userPage);
        }
        // 无缓存，就查询数据库
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
//        queryWrapper.ne("id", userId);
        userPage = userService.page(new Page<>(pageNum, pageSize), queryWrapper);
        //最后存入 redis
        try {
            valueOperations.set(redisKey, userPage, 30, TimeUnit.MINUTES);
        } catch (Exception e) {
            log.error("redis set key error", e);
        }
        return ResultUtils.success(userPage);
    }

    /**
     * 通过标签查找用户
     *
     * @param tagNameList 标签名称列表
     * @return 结果
     */
    @GetMapping("/search/tags")
    public BaseResponse<Page<User>> searchUserByTags(@RequestParam(required = false) List<String> tagNameList,
                                                     @RequestParam(required = false) String username,
                                                     @RequestParam(value = "pageSize", required = true) long pageSize,
                                                     @RequestParam(value = "pageNum", required = true) long pageNum
    ) {
        String key = SEARCH_TAGS + tagNameList + ":" + pageNum;
        //1.有缓存，读缓存
        ValueOperations<String, Object> valueOperations = redisTemplate.opsForValue();
        Page<User> userPage = (Page<User>) valueOperations.get(key);
        if (userPage != null) {
            return ResultUtils.success(userPage);
        }
        //2.没缓存，查数据库
        Page<User> list = userService.searchUsersByTags(tagNameList, username, pageSize, pageNum);
        //3.最后存入 redis
        try {
            valueOperations.set(key, list, 30, TimeUnit.MINUTES);
        } catch (Exception e) {
            log.error("redis set key error", e);
        }
        return ResultUtils.success(list);
    }

//    @GetMapping("/search/tags")
//    public BaseResponse<List<User>> searchUsersByTags(@RequestParam(required = false) List<String> tagNameList) {
//        if (CollectionUtils.isEmpty(tagNameList)) {
//            throw new BusinessException(ErrorCode.PARAMS_ERROR);
//        }
//        List<User> userList = userService.searchUserByTags(tagNameList);
//        return ResultUtils.success(userList);
//    }

    /**
     * 删除用户信息
     *
     * @param id
     * @param request
     * @return
     */
    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteUser(@RequestBody long id, HttpServletRequest request) {
        if (!userService.isAdmin(request)) {
            throw new BusinessException(ErrorCode.NO_AUTH);
        }
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        boolean b = userService.removeById(id);
        return ResultUtils.success(b);
    }


    /**
     * 获取最匹配的用户
     */
    @GetMapping("/match")
    public BaseResponse<List<User>> matchUsers(long num, HttpServletRequest request) {
        if (num <= 0 || num > MAX_MATCH_USER_NUM) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "超出范围");
        }
        User loginUser = userService.getLoginUser(request);
        String key = MATCH_USER + loginUser.getId();
        ValueOperations<String, Object> valueOperations = redisTemplate.opsForValue();
        //1.有缓存，查缓存
        List<User> matchUsers = (List<User>) valueOperations.get(key);
        if (!CollectionUtils.isEmpty(matchUsers)) {
            return ResultUtils.success(matchUsers);
        }
        //2.没缓存，查数据库
        List<User> userList = userService.matchUsers(num, loginUser);
        //3.最后存 redis
        try {
            valueOperations.set(key, userList, 30, TimeUnit.MINUTES);
        } catch (Exception e) {
            log.error("redis set key error", e);
        }
        return ResultUtils.success(userList);
    }

    /**
     * 根据用户id查询用户信息
     *
     * @param id
     * @return
     */
    @GetMapping("/getUserById")
    public BaseResponse<User> getUserById(Integer id) {
        User user = userService.getUserById(id);
        User safetyUser = userService.getSafetyUser(user);
        return ResultUtils.success(safetyUser);
    }


}
