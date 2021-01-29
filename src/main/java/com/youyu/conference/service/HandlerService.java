package com.youyu.conference.service;

import com.qcloud.cos.COSClient;
import com.qcloud.cos.model.ciModel.snapshot.SnapshotRequest;
import com.youyu.conference.common.GeneratorID;
import com.youyu.conference.common.PrizeConfig;
import com.youyu.conference.config.TencentConfig;
import com.youyu.conference.entity.UserEnrollWork;
import com.youyu.conference.entity.UserPrizeRecord;
import com.youyu.conference.repository.UserEnrollWorkMapper;
import com.youyu.conference.repository.UserPrizeRecordMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.Objects;

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
        Integer recordType = event.getRecordType();
        UserEnrollWork record = event.getRecord();
        String workUrl = record.getWorkUrl();
        String suffixName = workUrl.substring(workUrl.lastIndexOf(".")).toLowerCase();
        if (VIDEO_TYPE_DICT.contains(suffixName)) {
            COSClient cc = TencentConfig.intiClient();
            String headImgKey = StringUtils.join(PREFIX, File.separator, RandomStringUtils.randomAlphanumeric(32), ".jpg");
            SnapshotRequest request = new SnapshotRequest();
            //2.添加请求参数 参数详情请见api接口文档
            request.setBucketName(TencentConfig.BUCKET_NAME);
            request.getInput().setObject(workUrl);
            request.getOutput().setBucket(TencentConfig.BUCKET_NAME);
            request.getOutput().setRegion(TencentConfig.REGION_ID);
            request.getOutput().setObject(headImgKey);
            request.setTime("1");//视频第一秒
            cc.generateSnapshot(request);
            cc.shutdown();
            record.setWorkHeadImg(headImgKey);
        }
        if (Objects.equals(recordType, NumberUtils.INTEGER_ONE)) {
            record.setId(GeneratorID.getId());
            userEnrollWorkMapper.insertSelective(record);
        }
        if (Objects.equals(recordType, NumberUtils.INTEGER_TWO)) {
            userEnrollWorkMapper.updateByPrimaryKeySelective(record);
        }

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
