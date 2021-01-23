package com.youyu.conference.repository.biz;

import com.youyu.conference.web.vm.ScoreBillboardVM;
import com.youyu.conference.web.vm.UserInfoVM;
import com.youyu.conference.web.vm.UserQueryParams;
import com.youyu.conference.web.vm.WorkEnrollListOutVM;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ActivityUserBizMapper {

    UserInfoVM selectUserInfo(@Param("userId") Long userId);

    List<ScoreBillboardVM> selectScoreBillboard();

    Long selectMyBillboard(@Param("userId") Long userId);

    List<WorkEnrollListOutVM> selectWorkList(@Param("type") Integer type,
                                             @Param("state") Integer state);

    List<UserInfoVM> selectUserList(@Param("queryParams") UserQueryParams userQueryParams);

}