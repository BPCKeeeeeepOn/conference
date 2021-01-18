package com.youyu.conference.web.vm;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import jdk.nashorn.internal.objects.annotations.Getter;
import lombok.Data;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Objects;

import static com.youyu.conference.common.ConferenceConstants.PRIZE_TYPE.PRIZE_COUPON;
import static com.youyu.conference.common.ConferenceConstants.PRIZE_TYPE.PRIZE_REDPACK;

@Data
public class UserInfoVM {

    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    private String userName;

    private String userNumber;

    private String userCity;

    private Integer userCountry; //国家 1.国内 2.国外

    private Integer workStatus; //作品审核 0:待审核 10:审核通过 20:审核拒绝 30:未上传作品

    private Integer voteCount; //剩余投票次数

    private Integer score;

    private Long redpackCount;

    private Long couponCount;

    private List<ScoreDetailVM> scoreRecordList;

    private List<PrizeDetailVM> prizeRecordList;

    @Getter(name = "score")
    public Integer getScore() {
        if (!CollectionUtils.isEmpty(scoreRecordList)) {
            return scoreRecordList.stream().mapToInt(ScoreDetailVM::getScore).sum(); //积分总和
        }
        return NumberUtils.INTEGER_ZERO;
    }

    @Getter(name = "redpackCount")
    public Long getRedpackCount() {
        if (!CollectionUtils.isEmpty(prizeRecordList)) {
            return prizeRecordList.stream().filter(item -> Objects.equals(item.getPrizeType(), PRIZE_REDPACK)).count();
        }
        return NumberUtils.LONG_ZERO;
    }

    @Getter(name = "couponCount")
    public Long getCouponCount() {
        if (!CollectionUtils.isEmpty(prizeRecordList)) {
            return prizeRecordList.stream().filter(item -> Objects.equals(item.getPrizeType(), PRIZE_COUPON)).count();
        }
        return NumberUtils.LONG_ZERO;
    }
}
