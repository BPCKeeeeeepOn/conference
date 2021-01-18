package com.youyu.conference.repository.biz;

import com.youyu.conference.web.vm.ScoreBillboardVM;
import com.youyu.conference.web.vm.UserInfoVM;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface ActivityUserBizMapper {

    UserInfoVM selectUserInfo(@Param("userId") Long userId);

    List<ScoreBillboardVM> selectScoreBillboard();
}