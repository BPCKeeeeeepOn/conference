package com.youyu.conference.service;

import com.github.pagehelper.PageHelper;
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
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.youyu.conference.common.ConferenceConstants.EVENT_TYPE.EVENT_TYPE_3;
import static com.youyu.conference.common.ConferenceConstants.EVENT_TYPE.EVENT_TYPE_DISC;
import static com.youyu.conference.common.ConferenceConstants.USER_STATUS.USER_STATUS;
import static com.youyu.conference.common.ConferenceConstants.WORK_TYPE.WORK_TYPE_1;
import static com.youyu.conference.utils.CommonUtils.PREFIX;

@Service
@Slf4j
public class ActivityService {

    @Autowired
    public ApplicationEventPublisher eventPublisher;

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
        UserInfoVM userInfo = getUserInfo(userId);
        return userInfo;
    }

    /**
     * 获取用户信息(根据userId)
     *
     * @param userId
     * @return
     */
    public UserInfoVM getUserInfo(Long userId) {
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
    public List<ScoreBillboardVM> getScoreBillboard(Integer offset, Integer limit) {
        PageHelper.offsetPage(offset, limit);
        List<ScoreBillboardVM> scoreList = activityUserBizMapper.selectScoreBillboard();
        return scoreList;
    }

    public Map<String, Long> getMyBillboard() {
        long currUserId = currentUserUtils.getCurrUserId();
        Long round = activityUserBizMapper.selectMyBillboard(currUserId);
        return new HashMap<String, Long>() {{
            put("round", round);
        }};
    }

    public void handlerCommitWork(WorkEnrollInVM workEnrollInVM) {
        long currUserId = currentUserUtils.getCurrUserId();
        eventPublisher.publishEvent(new ReportDataEvent(this, workEnrollInVM, currUserId));
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
    public List<WorkEnrollListOutVM> getWorkList(Integer type, Integer state, Integer offset, Integer limit) {
        if (!EVENT_TYPE_DISC.contains(type)) {
            throw new BizException(ResponseResult.fail(ResultCode.PARAMS_ERROR));
        }
        PageHelper.offsetPage(offset, limit);
        List<WorkEnrollListOutVM> result = activityUserBizMapper.selectWorkList(type,state);
        return result;
    }

    /**
     * 获取作品key
     *
     * @return
     */
    public Map<String, String> getKey() {
        return new HashMap<String, String>() {{
            put("key", StringUtils.join(PREFIX, File.separator, RandomStringUtils.randomAlphanumeric(32)));
        }};
    }

    /**
     * 用户行为
     *
     * @param workId
     * @param event
     */
    @Transactional("dataSourceTransactionManager")
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
            addScore(receiveUserId, scoreInVM);
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
    public void addScore(Long userId, ScoreInVM scoreInVM) {
        UserScoreRecord record = new UserScoreRecord();
        record.setId(GeneratorID.getId());
        record.setUserId(userId);
        record.setScore(scoreInVM.getScore());
        record.setScoreChannel(scoreInVM.getChannel());
        userScoreRecordMapper.insertSelective(record);
    }

    /**
     * 审核作品
     *
     * @param id
     * @param state
     */
    public void examineWork(Long id, Integer state) {
        UserEnrollWork record = userEnrollWorkMapper.selectByPrimaryKey(id);
        if (Objects.isNull(record)) {
            throw new BizException(ResponseResult.fail(ResultCode.CODE1003));
        }
        if (!USER_STATUS.contains(state)) {
            throw new BizException(ResponseResult.fail(ResultCode.PARAMS_ERROR));
        }
        Integer workType = record.getWorkType();
        if (Objects.equals(workType, WORK_TYPE_1)) {//家的味道，新增100积分
            ScoreInVM scoreInVM = new ScoreInVM();
            scoreInVM.setChannel("参与家的味道");
            scoreInVM.setScore(100);
            addScore(record.getUserId(), scoreInVM);
        }
        record.setWorkStatus(state);
        userEnrollWorkMapper.updateByPrimaryKeySelective(record);
    }

    /**
     * 查询用户列表
     *
     * @param userQueryParams
     * @return
     */
    public List<UserInfoVM> getUserList(UserQueryParams userQueryParams, int offset, int limit) {
        PageHelper.offsetPage(offset, limit);
        List<UserInfoVM> userInfoVMS = activityUserBizMapper.selectUserList(userQueryParams);
        return userInfoVMS;
    }


}
