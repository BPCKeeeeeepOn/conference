package com.youyu.conference.service;

import com.github.pagehelper.PageHelper;
import com.youyu.conference.common.GeneratorID;
import com.youyu.conference.common.PrizeConfig;
import com.youyu.conference.common.ResponseResult;
import com.youyu.conference.common.ResultCode;
import com.youyu.conference.entity.*;
import com.youyu.conference.exception.BizException;
import com.youyu.conference.repository.*;
import com.youyu.conference.repository.biz.ActivityUserBizMapper;
import com.youyu.conference.utils.CommonUtils;
import com.youyu.conference.utils.CurrentUserUtils;
import com.youyu.conference.web.vm.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.json.JSONObject;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static com.youyu.conference.common.ConferenceConstants.EVENT_TYPE.EVENT_TYPE_3;
import static com.youyu.conference.common.ConferenceConstants.EVENT_TYPE.EVENT_TYPE_DISC;
import static com.youyu.conference.common.ConferenceConstants.USER_STATUS.*;
import static com.youyu.conference.common.ConferenceConstants.WORK_TYPE.*;

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
    private UserDrawRecordMapper userDrawRecordMapper;

    @Autowired
    private ActivityUserBizMapper activityUserBizMapper;

    @Autowired
    private CustomUserMapper customUserMapper;

    @Autowired
    private HandlerService handlerService;

    @Autowired
    private RedisTemplate redisTemplate;

    static final String PERSONA_LDRAW_LUCK_COUNT_KEY = "PERSONAL_LRAW_LUCK_COUNT_";

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

    /**
     * 我的排行榜
     *
     * @return
     */
    public Map<String, Long> getMyBillboard() {
        long currUserId = currentUserUtils.getCurrUserId();
        Long round = activityUserBizMapper.selectMyBillboard(currUserId);
        return new HashMap<String, Long>() {{
            put("round", round);
        }};
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
                    record.setWorkStatus(DEFAULT_STATUS);
                    eventPublisher.publishEvent(new EnrollWorkDataEvent(this, NumberUtils.INTEGER_TWO, record, currUserId));
                    return;
                }
            }
        }
        if ((Objects.equals(WORK_TYPE_2, workType))) {
            example.clear();
            example.createCriteria().andUserIdEqualTo(currUserId).andWorkTypeEqualTo(WORK_TYPE_2).andIsDeletedEqualTo(false);
            example.setOrderByClause(" created_time desc");
            List<UserEnrollWork> userEnrollWorks2 = userEnrollWorkMapper.selectByExample(example);
            if (!CollectionUtils.isEmpty(userEnrollWorks2)) {
                LocalDateTime now = LocalDateTime.now().minusMinutes(10L);
                //获取最近一次上传作品的时间
                LocalDateTime createdTime = userEnrollWorks2.get(NumberUtils.INTEGER_ZERO).getCreatedTime();
                if (now.isBefore(createdTime)) { //对比时间
                    throw new BizException(ResponseResult.fail(ResultCode.CODE1009));
                }
            }
        }
        eventPublisher.publishEvent(new EnrollWorkDataEvent(this, NumberUtils.INTEGER_ONE, record, currUserId));
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
    public List<WorkEnrollListOutVM> getWorkList(Integer type, Integer state, Integer scene, Integer offset, Integer limit) {
        if (!WORK_TYPE_DISC.contains(type)) {
            throw new BizException(ResponseResult.fail(ResultCode.PARAMS_ERROR));
        }
        PageHelper.offsetPage(offset, limit);
        List<WorkEnrollListOutVM> result = activityUserBizMapper.selectWorkList(type, state, scene);
        return result;
    }

    /**
     * 获取作品key
     *
     * @return
     */
    public Map<String, Object> getKey() {
        JSONObject qcloudCredential = CommonUtils.getQcloudCredential();
        return qcloudCredential.toMap();
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
     * 用户抽奖
     *
     * @param round
     */
    public Map<String, Object> personalDrawLuck(Integer round) {
        Map<String, Object> result = new HashMap<>();
        if (!EVENT_TYPE_DISC.contains(round)) {
            throw new BizException(ResponseResult.fail(ResultCode.PARAMS_ERROR));
        }
        long currUserId = currentUserUtils.getCurrUserId();
        String key = StringUtils.join(PERSONA_LDRAW_LUCK_COUNT_KEY, round, "_", (String.valueOf(currUserId)));
        Integer count = (Integer) redisTemplate.opsForValue().get(key);
        count = Objects.isNull(count) ? NumberUtils.INTEGER_ZERO : count;
        if (count >= 10) {
            throw new BizException(ResponseResult.fail(ResultCode.CODE1015));
        }
        //写入抽奖次数
        redisTemplate.opsForValue().increment(key, NumberUtils.INTEGER_ONE);
        //执行抽奖
        PrizeConfig prizeConfig = CommonUtils.personalDrawLuck();
        if (Objects.equals(prizeConfig.getIndex(), NumberUtils.INTEGER_ZERO)) {//未中奖
            throw new BizException(ResponseResult.fail(ResultCode.CODE1017));
        }
        String prizeKey = prizeConfig.getPrizeKey();
        //判断奖池是否抽完
        Integer prizeCount = (Integer) redisTemplate.opsForValue().get(prizeKey);
        prizeCount = Objects.isNull(prizeCount) ? NumberUtils.INTEGER_ZERO : prizeCount;
        if (prizeCount >= prizeConfig.getCount()) {
            throw new BizException(ResponseResult.fail(ResultCode.CODE1017));
        }
        result.put("type", prizeConfig.getPrizeType());
        result.put("name", prizeConfig.getPrizeName());
        //写入中奖次数
        redisTemplate.opsForValue().increment(prizeConfig.getPrizeKey(), NumberUtils.INTEGER_ONE);
        //记录抽奖
        eventPublisher.publishEvent(new PrizeDataEvent(this, prizeConfig, currUserId));
        return result;
    }

    /**
     * 大屏幕抽奖
     *
     * @param round
     */
    public List<CustomUser> screenDrawLuck(Integer type, Integer round, Integer count, String userCity) {
        List<CustomUser> randomUserList = new ArrayList<>();
        //查询已经中奖的用户
        UserDrawRecordExample userDrawRecordExample = new UserDrawRecordExample();
        userDrawRecordExample.createCriteria().andDrawTypeEqualTo(2).andIsDeletedEqualTo(false);
        List<UserDrawRecord> userDrawRecords = userDrawRecordMapper.selectByExample(userDrawRecordExample);
        List<Long> userIds = userDrawRecords.stream().map(UserDrawRecord::getUserId).collect(Collectors.toList());
        if (Objects.equals(type, 2)) { //多人抽奖
            //查询不包含中奖的用户
            CustomUserExample customUserExample = new CustomUserExample();
            customUserExample.createCriteria().andIdNotIn(CollectionUtils.isEmpty(userIds) ? new ArrayList<Long>() {{
                add(NumberUtils.LONG_ZERO);
            }} : userIds).andIsDeletedEqualTo(false);
            List<CustomUser> customUsers = customUserMapper.selectByExample(customUserExample);
            randomUserList = CommonUtils.getRandomList(customUsers, count);

        }
        if (Objects.equals(type, 3)) { //单人抽奖
            if (StringUtils.isBlank(userCity)) {
                throw new BizException(ResponseResult.fail(ResultCode.PARAMS_ERROR));
            }
            CustomUserExample customUserExample = new CustomUserExample();
            customUserExample.createCriteria().andIdNotIn(CollectionUtils.isEmpty(userIds) ? new ArrayList<Long>() {{
                add(NumberUtils.LONG_ZERO);
            }} : userIds).andUserCityEqualTo(userCity).andIsDeletedEqualTo(false);
            List<CustomUser> customUsers = customUserMapper.selectByExample(customUserExample);
            if (CollectionUtils.isEmpty(customUsers)) {
                //如果没有用户，将从当前集群抽取所有用户
                customUserExample.clear();
                customUserExample.createCriteria().andUserCityEqualTo(userCity).andIsDeletedEqualTo(false);
                customUsers = customUserMapper.selectByExample(customUserExample);
            }
            //抽取一位幸运儿
            randomUserList = CommonUtils.getRandomList(customUsers, NumberUtils.INTEGER_ONE);
        }
        //存储记录
        randomUserList.stream().forEach(item -> {
            Long id = item.getId();
            UserDrawRecord record = new UserDrawRecord();
            record.setId(GeneratorID.getId());
            record.setUserId(id);
            record.setDrawType(type);
            record.setDrawRound(round);
            userDrawRecordMapper.insertSelective(record);
        });
        return randomUserList;
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
        if (!Objects.equals(DEFAULT_STATUS, record.getWorkStatus())) {
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

    /**
     * 获奖奖品列表
     *
     * @param prizeName
     * @param prizeType
     * @param offset
     * @param limit
     * @return
     */
    public List<PrizeDetailVM> getPrizeList(String prizeName, Integer prizeType, String userName, String userNumber, int offset, int limit) {
        PageHelper.offsetPage(offset, limit);
        List<PrizeDetailVM> prizeDetailVMS = activityUserBizMapper.selectPrizeList(prizeName, prizeType, userName, userNumber);
        return prizeDetailVMS;
    }

    /**
     * 积分列表
     *
     * @param userName
     * @param offset
     * @param limit
     * @return
     */
    public List<ScoreDetailVM> getScoreList(String userName, String userNumber, int offset, int limit) {
        PageHelper.offsetPage(offset, limit);
        List<ScoreDetailVM> scoreDetailVMS = activityUserBizMapper.selectScoreList(userName, userNumber);
        return scoreDetailVMS;
    }

    /**
     * 大屏幕抽奖记录
     *
     * @param prizeType
     * @param round
     * @param userName
     * @param userNumber
     * @param offset
     * @param limit
     * @return
     */
    public List<DrawLuckVM> getLuckList(Integer prizeType, Integer round, String userName, String userNumber, int offset, int limit) {
        PageHelper.offsetPage(offset, limit);
        List<DrawLuckVM> drawLuckVMS = activityUserBizMapper.selectLuckList(prizeType, round, userName, userNumber);
        return drawLuckVMS;
    }
}
