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
        Set<Integer> userStatus = Stream.of(DEFAULT_STATUS, PASS_STATUS, CANCEL_STATUS).collect(Collectors.toSet());
    }

    interface PRIZE_TYPE {
        int PRIZE_REDPACK = 1;
        int PRIZE_COUPON = 2;
    }

    interface WORK_TYPE {
        int WORK_TYPE_1 = 1;
        int WORK_TYPE_2 = 2;
        Set<Integer> WORK_TYPE_DISC = Stream.of(WORK_TYPE_1, WORK_TYPE_2).collect(Collectors.toSet());
    }

    enum UnreadActionType {
        MESSAGE(1),
        CART(2);

        int code;

        UnreadActionType(int code) {
            this.code = code;
        }

        public int getCode() {
            return this.code;
        }
    }

    String UNREAD_GROUP_KEY = "unread_message_key_";

    String UNREAD_MSG_COUNT = "unread_msg_count";

    /**
     * 即时通讯发送内容key
     */
    String CHAT_RECEIVE_KEY = "chat_receive_key_";

    String DOWNLOAD_URL = "https://service.dcloud.net.cn/build/download/f4187b60-24af-11ea-b536-91ba8bd01347";

}
