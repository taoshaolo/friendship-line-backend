package com.taoshao.friendshipline.mapper;

import com.taoshao.friendshipline.model.domain.Team;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Update;

/**
 * @Entity com.taoshao.friendshipline.model.domain.Team
 */
public interface TeamMapper extends BaseMapper<Team> {


    /**
     * 增加队伍加入人数
     *
     * @param teamId 队伍ID
     * @return 是否成功
     */
    @Update("update team set joinNum = joinNum + 1 where id = #{teamId}")
    boolean addTeamJoinNum(Long teamId);
}




