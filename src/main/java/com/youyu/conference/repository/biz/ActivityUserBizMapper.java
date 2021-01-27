package com.youyu.conference.repository.biz;

import com.youyu.conference.web.vm.*;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ActivityUserBizMapper {

    UserInfoVM selectUserInfo(@Param("userId") Long userId);

    List<ScoreBillboardVM> selectScoreBillboard();

    Long selectMyBillboard(@Param("userId") Long userId);

    List<WorkEnrollListOutVM> selectWorkList(@Param("type") Integer type,
                                             @Param("state") Integer state,
                                             @Param("scene") Integer scene);

    List<UserInfoVM> selectUserList(@Param("queryParams") UserQueryParams userQueryParams);

    List<PrizeDetailVM> selectPrizeList(@Param("prizeName") String prizeName,
                                        @Param("prizeType") Integer prizeType,
                                        @Param("userName") String userName,
                                        @Param("userNumber") String userNumber);

    List<ScoreDetailVM> selectScoreList(@Param("userName") String userName,
                                        @Param("userNumber") String userNumber);

    List<DrawLuckVM> selectLuckList(@Param("drawType") Integer drawType,
                                    @Param("round") Integer round,
                                    @Param("userName") String userName,
                                    @Param("userNumber") String userNumber);

}