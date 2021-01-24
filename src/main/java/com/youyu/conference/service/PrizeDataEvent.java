package com.youyu.conference.service;

import com.youyu.conference.common.PrizeConfig;
import com.youyu.conference.entity.UserEnrollWork;
import org.springframework.context.ApplicationEvent;

public class PrizeDataEvent extends ApplicationEvent {


    private PrizeConfig prize;

    private Long currUserId;


    public PrizeDataEvent(Object source, PrizeConfig prize, Long currUserId) {
        super(source);
        this.prize = prize;
        this.currUserId = currUserId;
    }

    public PrizeConfig getPrize() {
        return prize;
    }

    public void setPrize(PrizeConfig prize) {
        this.prize = prize;
    }

    public Long getCurrUserId() {
        return currUserId;
    }

    public void setCurrUserId(Long currUserId) {
        this.currUserId = currUserId;
    }
}
