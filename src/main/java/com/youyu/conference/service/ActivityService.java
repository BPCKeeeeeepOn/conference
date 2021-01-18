package com.youyu.conference.service;

import com.youyu.conference.common.GeneratorID;
import com.youyu.conference.common.ResponseResult;
import com.youyu.conference.common.ResultCode;
import com.youyu.conference.entity.*;
import com.youyu.conference.exception.BizException;
import com.youyu.conference.repository.UserEnrollWorkMapper;
import com.youyu.conference.repository.UserPrizeRecordMapper;
import com.youyu.conference.repository.UserScoreRecordMapper;
import com.youyu.conference.repository.biz.ActivityUserBizMapper;
import com.youyu.conference.utils.CurrentUserUtils;
import com.youyu.conference.web.vm.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ActivityService {

    @Autowired
    private CurrentUserUtils currentUserUtils;

    @Autowired
    private UserScoreRecordMapper userScoreRecordMapper;

    @Autowired
    private UserPrizeRecordMapper userPrizeRecordMapper;

    @Autowired
    private UserEnrollWorkMapper userEnrollWorkMapper;

    @Autowired
    private ActivityUserBizMapper activityUserBizMapper;

    /**
     * 获取用户信息
     *
     * @return
     */
    public UserInfoVM getUserInfo() {
        Long userId = currentUserUtils.getCurrUserId();
        UserInfoVM result = activityUserBizMapper.selectUserInfo(userId);
        if (Objects.nonNull(result)) {
            result.setScoreRecordList(selectScoreRecord(userId));
            result.setPrizeRecordList(selectPrizeRecord(userId));
            return result;
        }
        return null;
    }

    /**
     * 查询积分排行榜
     *
     * @return
     */
    public Map<String, Object> getScoreBillboard() {
        Map<String, Object> result = new HashMap<>();
        long currUserId = currentUserUtils.getCurrUserId();
        List<ScoreBillboardVM> scoreList = activityUserBizMapper.selectScoreBillboard();
        if (!CollectionUtils.isEmpty(scoreList)) {
            List<Integer> collect = scoreList.stream().filter(item -> Objects.equals(currUserId, item.getUserId())).map(ScoreBillboardVM::getRownum).collect(Collectors.toList());
            Integer rank = collect.get(0);
            result.put("billboard", scoreList);
            result.put("current_rank", rank);
            return result;
        }
        return null;
    }

    /**
     * 提交作品
     *
     * @param workInVM
     */
    public void commitWork(WorkInVM workInVM) {
        long currUserId = currentUserUtils.getCurrUserId();
        //验证
        UserEnrollWorkExample example = new UserEnrollWorkExample();
        example.clear();
        example.createCriteria().andUserIdEqualTo(currUserId).andWorkTypeEqualTo(workInVM.getWorkType()).andIsDeletedEqualTo(false);
        List<UserEnrollWork> userEnrollWorks = userEnrollWorkMapper.selectByExample(example);
        if(!CollectionUtils.isEmpty(userEnrollWorks)){
            throw new BizException(ResponseResult.fail(ResultCode.WORK_EXISZTS));
        }
        UserEnrollWork record = workInVM.buildInsertRecord();
        record.setId(GeneratorID.getId());
        record.setUserId(currUserId);
        userEnrollWorkMapper.insertSelective(record);
    }

    /*
    获取积分列表(个人)
     */
    public List<ScoreDetailVM> selectScoreRecord(Long userId) {
        UserScoreRecordExample example = new UserScoreRecordExample();
        example.clear();
        example.createCriteria().andUserIdEqualTo(userId).andIsDeletedEqualTo(false);
        example.setOrderByClause(" created_time desc");
        List<UserScoreRecord> userScoreRecords = userScoreRecordMapper.selectByExample(example);
        if (!CollectionUtils.isEmpty(userScoreRecords)) {
            List<ScoreDetailVM> result = userScoreRecords.stream().map(item -> {
                ScoreDetailVM scoreDetailVM = new ScoreDetailVM();
                BeanUtils.copyProperties(item, scoreDetailVM);
                return scoreDetailVM;
            }).collect(Collectors.toList());
            return result;
        }
        return null;
    }

    /*
     获取礼物列表(个人)
      */
    public List<PrizeDetailVM> selectPrizeRecord(Long userId) {
        UserPrizeRecordExample example = new UserPrizeRecordExample();
        example.clear();
        example.createCriteria().andUserIdEqualTo(userId).andIsDeletedEqualTo(false);
        example.setOrderByClause(" prize_type asc, created_time desc");
        List<UserPrizeRecord> userPrizeRecords = userPrizeRecordMapper.selectByExample(example);
        if (!CollectionUtils.isEmpty(userPrizeRecords)) {
            List<PrizeDetailVM> result = userPrizeRecords.stream().map(item -> {
                PrizeDetailVM prizeDetailVM = new PrizeDetailVM();
                BeanUtils.copyProperties(item, prizeDetailVM);
                return prizeDetailVM;
            }).collect(Collectors.toList());
            return result;
        }
        return null;
    }

}
