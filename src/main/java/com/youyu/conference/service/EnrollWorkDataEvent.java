package com.youyu.conference.service;

import com.youyu.conference.entity.UserEnrollWork;
import org.springframework.context.ApplicationEvent;

public class EnrollWorkDataEvent extends ApplicationEvent {


    private Integer recordType;

    private UserEnrollWork record;

    private Long currUserId;


    public EnrollWorkDataEvent(Object source, Integer recordType, UserEnrollWork record, Long currUserId) {
        super(source);
        this.recordType = recordType;
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

    public Integer getRecordType() {
        return recordType;
    }

    public void setRecordType(Integer recordType) {
        this.recordType = recordType;
    }
}
