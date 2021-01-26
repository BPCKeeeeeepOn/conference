package com.youyu.conference.service;

import com.qcloud.cos.COSClient;
import com.qcloud.cos.model.ciModel.snapshot.SnapshotRequest;
import com.youyu.conference.common.GeneratorID;
import com.youyu.conference.common.PrizeConfig;
import com.youyu.conference.common.ResponseResult;
import com.youyu.conference.common.ResultCode;
import com.youyu.conference.config.TencentConfig;
import com.youyu.conference.entity.UserEnrollWork;
import com.youyu.conference.entity.UserEnrollWorkExample;
import com.youyu.conference.entity.UserPrizeRecord;
import com.youyu.conference.exception.BizException;
import com.youyu.conference.repository.UserEnrollWorkMapper;
import com.youyu.conference.repository.UserPrizeRecordMapper;
import com.youyu.conference.web.vm.WorkEnrollInVM;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.io.File;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import static com.youyu.conference.common.ConferenceConstants.WORK_TYPE.WORK_TYPE_1;
import static com.youyu.conference.common.ConferenceConstants.WORK_TYPE.WORK_TYPE_2;
import static com.youyu.conference.utils.CommonUtils.PREFIX;
import static com.youyu.conference.utils.CommonUtils.VIDEO_TYPE_DICT;

@Service
@Slf4j
public class HandlerService {

    @Autowired
    private UserEnrollWorkMapper userEnrollWorkMapper;

    @Autowired
    private UserPrizeRecordMapper userPrizeRecordMapper;

    /**
     * 提交作品
     */
    @Async("asyncChatExecutor")
    @EventListener(classes = EnrollWorkDataEvent.class)
    public void commitWork(EnrollWorkDataEvent event) {
        UserEnrollWork record = event.getRecord();
        record.setId(GeneratorID.getId());
        userEnrollWorkMapper.insertSelective(record);
    }

    @Async("asyncChatExecutor")
    @EventListener(classes = PrizeDataEvent.class)
    public void insertPrize(PrizeDataEvent event) {
        Long currUserId = event.getCurrUserId();
        PrizeConfig prizeConfig = event.getPrize();
        UserPrizeRecord record = new UserPrizeRecord();
        record.setId(GeneratorID.getId());
        record.setUserId(currUserId);
        record.setPrizeType(prizeConfig.getPrizeType());
        record.setPrizeName(prizeConfig.getPrizeName());
        userPrizeRecordMapper.insertSelective(record);
    }
}