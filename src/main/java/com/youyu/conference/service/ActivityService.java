package com.youyu.conference.service;

import com.youyu.conference.common.GeneratorID;
import com.youyu.conference.common.ResponseResult;
import com.youyu.conference.common.ResultCode;
import com.youyu.conference.entity.*;
import com.youyu.conference.exception.BizException;
import com.youyu.conference.repository.UserEnrollWorkEventMapper;
import com.youyu.conference.repository.UserEnrollWorkMapper;
import com.youyu.conference.repository.UserPrizeRecordMapper;
import com.youyu.conference.repository.UserScoreRecordMapper;
import com.youyu.conference.repository.biz.ActivityUserBizMapper;
import com.youyu.conference.utils.CurrentUserUtils;
import com.youyu.conference.web.vm.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.youyu.conference.common.ConferenceConstants.EVENT_TYPE.*;
import static com.youyu.conference.common.ConferenceConstants.USER_STATUS.DEFAULT_STATUS;
import static com.youyu.conference.common.ConferenceConstants.USER_STATUS.PASS_STATUS;
import static com.youyu.conference.common.ConferenceConstants.WORK_TYPE.WORK_TYPE_1;
import static com.youyu.conference.common.ConferenceConstants.WORK_TYPE.WORK_TYPE_2;

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
    private UserEnrollWorkEventMapper userEnrollWorkEventMapper;

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
     * @param workEnrollInVM
     */
    public void commitWork(WorkEnrollInVM workEnrollInVM) {
        long currUserId = currentUserUtils.getCurrUserId();
        UserEnrollWork record = workEnrollInVM.buildInsertRecord();
        record.setUserId(currUserId);
        Integer workType = workEnrollInVM.getWorkType();
        //验证
        UserEnrollWorkExample example = new UserEnrollWorkExample();
        if (Objects.equals(WORK_TYPE_1, workType)) {
            example.clear();
            example.createCriteria().andUserIdEqualTo(currUserId).andWorkTypeEqualTo(WORK_TYPE_1).andIsDeletedEqualTo(false);
            List<UserEnrollWork> userEnrollWorks1 = userEnrollWorkMapper.selectByExample(example);
            if (!CollectionUtils.isEmpty(userEnrollWorks1)) {
                UserEnrollWork userEnrollWork1 = userEnrollWorks1.get(NumberUtils.INTEGER_ZERO);
                if (Objects.equals(userEnrollWork1.getWorkStatus(), PASS_STATUS) || Objects.equals(userEnrollWork1.getWorkStatus(), DEFAULT_STATUS)) {
                    throw new BizException(ResponseResult.fail(ResultCode.CODE1001));
                } else {
                    record.setId(userEnrollWork1.getId());
                    userEnrollWorkMapper.updateByPrimaryKey(record);
                    return;
                }
            }
            record.setId(GeneratorID.getId());
        }
        if ((Objects.equals(WORK_TYPE_2, workType))) {
            example.clear();
            example.createCriteria().andUserIdEqualTo(currUserId).andWorkTypeEqualTo(WORK_TYPE_2).andIsDeletedEqualTo(false);
            example.setOrderByClause(" created_time desc");
            List<UserEnrollWork> userEnrollWorks2 = userEnrollWorkMapper.selectByExample(example);
            if (!CollectionUtils.isEmpty(userEnrollWorks2)) {
                LocalDateTime now = LocalDateTime.now();
                //获取最近一次上传作品的时间
                LocalDateTime createdTime = userEnrollWorks2.get(NumberUtils.INTEGER_ZERO).getCreatedTime();
                if (now.isAfter(createdTime)) { //对比时间
                    throw new BizException(ResponseResult.fail(ResultCode.CODE1009));
                }
            }
        }
        record.setId(GeneratorID.getId());
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

    /**
     * 作品列表
     *
     * @param type
     * @return
     */
    public List<WorkEnrollListOutVM> getWorkList(Integer type) {
        if (!EVENT_TYPE_DISC.contains(type)) {
            throw new BizException(ResponseResult.fail(ResultCode.PARAMS_ERROR));
        }
        List<WorkEnrollListOutVM> result = activityUserBizMapper.selectWorkList(type);
        return result;
    }

    /**
     * 用户行为
     *
     * @param workId
     * @param event
     */
    public void recordEventWork(Long workId, Integer event) {
        //检查作品信息
        UserEnrollWork userEnrollWork = userEnrollWorkMapper.selectByPrimaryKey(workId);
        if (Objects.isNull(userEnrollWork)) {
            throw new BizException(ResponseResult.fail(ResultCode.PARAMS_ERROR));
        }
        //获取当前用户信息
        Long currUserId = currentUserUtils.getCurrUserId();
        Long receiveUserId = userEnrollWork.getUserId();
        //判断投票次数
        if (Objects.equals(userEnrollWork.getWorkType(), WORK_TYPE_1) && Objects.equals(event, EVENT_TYPE_3)) {
            if (Objects.equals(currUserId, receiveUserId)) {
                throw new BizException(ResponseResult.fail(ResultCode.CODE1013));
            }
            //判断投票是否超过两次
            UserEnrollWorkEventExample example = new UserEnrollWorkEventExample();
            example.createCriteria().andSendUserIdEqualTo(currUserId).andEventTypeEqualTo(EVENT_TYPE_3).andIsDeletedEqualTo(false);
            if (userEnrollWorkEventMapper.countByExample(example) >= 2L) {
                throw new BizException(ResponseResult.fail(ResultCode.CODE1011));
            }
            //todo 新增积分
            ScoreInVM scoreInVM = new ScoreInVM();
            scoreInVM.setChannel("投票");
            scoreInVM.setScore(30);
            addScore(scoreInVM);
        }
        UserEnrollWorkEvent record = new UserEnrollWorkEvent();
        record.setEventType(event);
        record.setId(GeneratorID.getId());
        record.setWorkId(workId);
        record.setSendUserId(currUserId);
        record.setReceiveUserId(receiveUserId);
        userEnrollWorkEventMapper.insertSelective(record);
    }

    /**
     * 新增积分
     *
     * @param scoreInVM
     */
    public void addScore(ScoreInVM scoreInVM) {
        Long currUserId = currentUserUtils.getCurrUserId();
        UserScoreRecord record = new UserScoreRecord();
        record.setId(GeneratorID.getId());
        record.setUserId(currUserId);
        record.setScore(scoreInVM.getScore());
        record.setScoreChannel(scoreInVM.getChannel());
        userScoreRecordMapper.insertSelective(record);
    }
}
