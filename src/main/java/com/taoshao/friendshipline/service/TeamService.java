package com.taoshao.friendshipline.service;

import com.taoshao.friendshipline.model.domain.Team;
import com.baomidou.mybatisplus.extension.service.IService;
import com.taoshao.friendshipline.model.domain.User;
import com.taoshao.friendshipline.model.dto.TeamQuery;
import com.taoshao.friendshipline.model.request.TeamJoinRequest;
import com.taoshao.friendshipline.model.request.TeamQuitRequest;
import com.taoshao.friendshipline.model.request.TeamUpdateRequest;
import com.taoshao.friendshipline.model.vo.TeamUserVo;

import java.util.List;

/**
 *
 */
public interface TeamService extends IService<Team> {

    /**
     * 创建队伍
     * @param team
     * @param loginUser
     * @return
     */
    long addTeam(Team team, User loginUser);

    /**
     * 搜索队伍
     *
     * @param teamQuery
     * @param isAdmin
     * @return
     */
    List<TeamUserVo> listTeams(TeamQuery teamQuery, boolean isAdmin);

    /**
     * 更新队伍
     *
     * @param teamUpdateRequest
     * @param loginUser
     * @return
     */
    boolean updateTeam(TeamUpdateRequest teamUpdateRequest,User loginUser);

    /**
     * 加入队伍
     * @param teamJoinRequest
     * @param loginUser
     * @return
     */
    boolean joinTeam(TeamJoinRequest teamJoinRequest, User loginUser);

    /**
     * 退出队伍
     * @param teamQuitRequest
     * @param loginUser
     * @return
     */
    boolean quitTeam(TeamQuitRequest teamQuitRequest, User loginUser);

    /**
     * 删除解散队伍
     *
     * @param id
     * @param loginUser
     * @return
     */
    boolean deleteTeam(Long id, User loginUser);
}
