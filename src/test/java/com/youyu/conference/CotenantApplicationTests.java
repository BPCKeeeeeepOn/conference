/*
package com.youyu.conference;

import com.youyu.conference.common.GeneratorID;
import com.youyu.conference.entity.UserEnrollWork;
import com.youyu.conference.entity.UserScoreRecord;
import com.youyu.conference.repository.UserEnrollWorkMapper;
import com.youyu.conference.repository.UserScoreRecordMapper;
import com.youyu.conference.repository.biz.ActivityUserBizMapper;
import com.youyu.conference.web.vm.UserInfoVM;
import com.youyu.conference.web.vm.UserQueryParams;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("dev")
public class CotenantApplicationTests {

    @Autowired
    private ActivityUserBizMapper activityUserBizMapper;

    @Autowired
    private UserEnrollWorkMapper userEnrollWorkMapper;

    @Autowired
    private UserScoreRecordMapper userScoreRecordMapper;

    @Test
    public void testInsertWork() {
        List<UserInfoVM> userInfoVMS = activityUserBizMapper.selectUserList(new UserQueryParams());
        userInfoVMS.stream().forEach(item -> {
            UserEnrollWork record = new UserEnrollWork();
            record.setWorkStatus(10);
            record.setUserId(item.getId());
            record.setId(GeneratorID.getId());
            record.setWorkDesc(RandomStringUtils.randomAlphanumeric(18));
            record.setWorkType(RandomUtils.nextInt(1, 2));
            record.setWorkHeadImg(RandomStringUtils.randomAlphanumeric(18));
            record.setWorkUrl(RandomStringUtils.randomAlphanumeric(18));
            userEnrollWorkMapper.insertSelective(record);
        });
    }

    @Test
    public void testInsertScore() {
        List<UserInfoVM> userInfoVMS = activityUserBizMapper.selectUserList(new UserQueryParams());
        userInfoVMS.stream().forEach(item -> {
            UserScoreRecord record = new UserScoreRecord();
            record.setScoreChannel("小游戏");
            record.setScore(RandomUtils.nextInt(0, 100));
            record.setUserId(item.getId());
            record.setId(GeneratorID.getId());
            userScoreRecordMapper.insertSelective(record);
        });
    }

    public void testInsertEvent(){

    }
}
*/
