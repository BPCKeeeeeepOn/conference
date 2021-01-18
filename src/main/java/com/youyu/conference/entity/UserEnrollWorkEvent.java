package com.youyu.conference.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

public class UserEnrollWorkEvent implements Serializable {
    private Long id;

    private Long sendUserId;

    private Long receiveUserId;

    private Long workId;

    private Integer eventType;

    private LocalDateTime createdTime;

    private LocalDateTime updatedTime;

    private Boolean isDeleted;

    private static final long serialVersionUID = 1L;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getSendUserId() {
        return sendUserId;
    }

    public void setSendUserId(Long sendUserId) {
        this.sendUserId = sendUserId;
    }

    public Long getReceiveUserId() {
        return receiveUserId;
    }

    public void setReceiveUserId(Long receiveUserId) {
        this.receiveUserId = receiveUserId;
    }

    public Long getWorkId() {
        return workId;
    }

    public void setWorkId(Long workId) {
        this.workId = workId;
    }

    public Integer getEventType() {
        return eventType;
    }

    public void setEventType(Integer eventType) {
        this.eventType = eventType;
    }

    public LocalDateTime getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(LocalDateTime createdTime) {
        this.createdTime = createdTime;
    }

    public LocalDateTime getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(LocalDateTime updatedTime) {
        this.updatedTime = updatedTime;
    }

    public Boolean getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
    }
}