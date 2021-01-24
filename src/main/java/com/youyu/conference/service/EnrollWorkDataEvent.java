package com.youyu.conference.service;

import com.youyu.conference.entity.UserEnrollWork;
import org.springframework.context.ApplicationEvent;

public class EnrollWorkDataEvent extends ApplicationEvent {


    private UserEnrollWork record;

    private Long currUserId;


    public EnrollWorkDataEvent(Object source, UserEnrollWork record, Long currUserId) {
        super(source);
        this.record = record;
        this.currUserId = currUserId;
    }

    public UserEnrollWork getRecord() {
        return record;
    }

    public void setRecord(UserEnrollWork record) {
        this.record = record;
    }

    public Long getCurrUserId() {
        return currUserId;
    }

    public void setCurrUserId(Long currUserId) {
        this.currUserId = currUserId;
    }
}
