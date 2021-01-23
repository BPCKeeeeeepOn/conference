package com.youyu.conference.service;

import com.qcloud.cos.COSClient;
import com.qcloud.cos.model.ciModel.snapshot.SnapshotRequest;
import com.youyu.conference.common.GeneratorID;
import com.youyu.conference.common.ResponseResult;
import com.youyu.conference.common.ResultCode;
import com.youyu.conference.config.TencentConfig;
import com.youyu.conference.entity.UserEnrollWork;
import com.youyu.conference.entity.UserEnrollWorkExample;
import com.youyu.conference.exception.BizException;
import com.youyu.conference.repository.UserEnrollWorkMapper;
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

    /**
     * 提交作品
     */
    @Async("asyncChatExecutor")
    @EventListener(classes = ReportDataEvent.class)
    public void commitWork(ReportDataEvent event) {
        WorkEnrollInVM workEnrollInVM = event.getWorkEnrollInVM();
        Long currUserId = event.getCurrUserId();
        String workUrl = workEnrollInVM.getWorkUrl();
        UserEnrollWork record = workEnrollInVM.buildInsertRecord();
        String suffixName = workUrl.substring(workUrl.lastIndexOf("."));
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
        record.setUserId(currUserId);
        Integer workType = workEnrollInVM.getWorkType();
        //验证
        UserEnrollWorkExample example = new UserEnrollWorkExample();
        if (Objects.equals(WORK_TYPE_1, workType)) {
            example.clear();
            example.createCriteria().andUserIdEqualTo(currUserId).andWorkTypeEqualTo(WORK_TYPE_1).andIsDeletedEqualTo(false);
            List<UserEnrollWork> userEnrollWorks1 = userEnrollWorkMapper.selectByExample(example);
            if (!CollectionUtils.isEmpty(userEnrollWorks1)) {
                /*UserEnrollWork userEnrollWork1 = userEnrollWorks1.get(NumberUtils.INTEGER_ZERO);
                if (Objects.equals(userEnrollWork1.getWorkStatus(), PASS_STATUS) || Objects.equals(userEnrollWork1.getWorkStatus(), DEFAULT_STATUS)) {
                    throw new BizException(ResponseResult.fail(ResultCode.CODE1001));
                } else {
                    record.setId(userEnrollWork1.getId());
                    userEnrollWorkMapper.updateByPrimaryKey(record);
                    return;
                }*/
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
}
