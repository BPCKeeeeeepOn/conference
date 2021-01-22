package com.youyu.conference.common;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public interface ConferenceConstants {
    interface DATE_FORMATTER {
        String DATETIME_IN_CHINESE = "yyyy年MM月dd日 HH:mm:ss";
    }

    //默认分页数据大小
    int DEFAULT_PAGE_SIZE = 20;
    //默认起始页
    int DEFAULT_PAGE_OFFSET = 0;

    String FULL_DATE_TIME = "yyyy-MM-dd HH:mm:ss";

    String DEFAULT_PASSWORD = "123456";

    String CODE_CACHE = "code_";

    /*
    作品审核状态
     */
    interface USER_STATUS {
        int DEFAULT_STATUS = 0; //待审核
        int PASS_STATUS = 10; //审核通过
        int CANCEL_STATUS = 20; //审核拒绝
        Set<Integer> USER_STATUS = Stream.of(DEFAULT_STATUS, PASS_STATUS, CANCEL_STATUS).collect(Collectors.toSet());
    }

    interface PRIZE_TYPE {
        int PRIZE_REDPACK = 1; //红包
        int PRIZE_COUPON = 2; //优惠券
    }

    interface WORK_TYPE {
        int WORK_TYPE_1 = 1; //家的味道
        int WORK_TYPE_2 = 2; //精彩瞬间
        Set<Integer> WORK_TYPE_DISC = Stream.of(WORK_TYPE_1, WORK_TYPE_2).collect(Collectors.toSet());
    }

    interface EVENT_TYPE {
        int EVENT_TYPE_1 = 1; //阅读
        int EVENT_TYPE_2 = 2; //点赞
        int EVENT_TYPE_3 = 3; //投票
        Set<Integer> EVENT_TYPE_DISC = Stream.of(EVENT_TYPE_1, EVENT_TYPE_2, EVENT_TYPE_3).collect(Collectors.toSet());

    }
}
