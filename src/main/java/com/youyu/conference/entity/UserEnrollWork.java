package com.youyu.conference.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

public class UserEnrollWork implements Serializable {
    private Long id;

    private Long userId;

    private String workUrl;

    private String workHeadImg;

    private String workDesc;

    private Integer workType;

    private Integer workPv;

    private Integer workLike;

    private Integer workStatus;

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

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getWorkUrl() {
        return workUrl;
    }

    public void setWorkUrl(String workUrl) {
        this.workUrl = workUrl;
    }

    public String getWorkHeadImg() {
        return workHeadImg;
    }

    public void setWorkHeadImg(String workHeadImg) {
        this.workHeadImg = workHeadImg;
    }

    public String getWorkDesc() {
        return workDesc;
    }

    public void setWorkDesc(String workDesc) {
        this.workDesc = workDesc;
    }

    public Integer getWorkType() {
        return workType;
    }

    public void setWorkType(Integer workType) {
        this.workType = workType;
    }

    public Integer getWorkPv() {
        return workPv;
    }

    public void setWorkPv(Integer workPv) {
        this.workPv = workPv;
    }

    public Integer getWorkLike() {
        return workLike;
    }

    public void setWorkLike(Integer workLike) {
        this.workLike = workLike;
    }

    public Integer getWorkStatus() {
        return workStatus;
    }

    public void setWorkStatus(Integer workStatus) {
        this.workStatus = workStatus;
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