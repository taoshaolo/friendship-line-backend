package com.taoshao.friendshipline.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.taoshao.friendshipline.model.domain.User;
import com.baomidou.mybatisplus.extension.service.IService;
import com.taoshao.friendshipline.model.request.UserEditRequest;
import com.taoshao.friendshipline.model.request.UserTagAddRequest;
import com.taoshao.friendshipline.model.request.UserTagRemoveRequest;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 用户服务
 */
public interface UserService extends IService<User> {

    /**
     * 用户注册
     *
     * @param userAccount   用户账户
     * @param userPassword  用户密码
     * @param checkPassword 校验密码
     * @return 新用户 id
     */
    long userRegister(String userAccount, String userPassword, String checkPassword);

    /**
     * 用户登录
     *
     * @param userAccount  用户账户
     * @param userPassword 用户密码
     * @param request
     * @return 脱敏后的用户信息
     */
    User userLogin(String userAccount, String userPassword, HttpServletRequest request);

    /**
     * 用户脱敏
     *
     * @param originUser
     * @return
     */
    User getSafetyUser(User originUser);

    

    /**
     * 用户注销
     *
     * @param request
     * @return
     */
    int userLogout(HttpServletRequest request);

    /**
     * 根据标签搜索用户
     *
     * @param tagNameList
     * @return
     */
    List<User> searchUserByTags(List<String> tagNameList);

    /**
     * 更新用户信息
     *
     * @param user
     * @param loginUser
     * @return
     */
    int updateUser(UserEditRequest user, User loginUser);

    /**
     * 获取当前登录用户信息
     *
     * @param request
     * @return
     */
    User getLoginUser(HttpServletRequest request);

    /**
     * 根据id查询用户
     * @param id 用户id
     * @return User
     */
    User getUserById(Integer id);

    /**
     * 是否为管理员
     *
     * @param request
     * @return
     */
    boolean isAdmin(HttpServletRequest request);

    boolean isAdmin(User loginUser);

    /**
     * 获取最匹配的用户
     * @param num
     * @param loginUser
     * @return
     */
    List<User> matchUsers(long num, User loginUser);


    /**
     * 根据标签搜索用户
     * @param tagNameList
     * @param username
     * @param pageSize
     * @param pageNum
     * @return
     */
    Page<User> searchUsersByTags(List<String> tagNameList, String username, long pageSize, long pageNum);

    /**
     * 添加用户标签
     * @param userTagAddRequest
     * @return
     */
    Boolean addTag(UserTagAddRequest userTagAddRequest);

    /**
     * 删除用户标签
     *
     * @param userTagRemoveRequest
     * @return
     */
    Boolean removeTag(UserTagRemoveRequest userTagRemoveRequest);
}
