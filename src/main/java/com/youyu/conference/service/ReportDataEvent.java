package com.youyu.conference.service;

import com.youyu.conference.web.vm.WorkEnrollInVM;
import org.springframework.context.ApplicationEvent;

public class ReportDataEvent extends ApplicationEvent {


    private WorkEnrollInVM workEnrollInVM;

    private Long currUserId;


    public ReportDataEvent(Object source, WorkEnrollInVM workEnrollInVM, Long currUserId) {
        super(source);
        this.workEnrollInVM = workEnrollInVM;
        this.currUserId = currUserId;
    }

    public WorkEnrollInVM getWorkEnrollInVM() {
        return workEnrollInVM;
    }

    public void setWorkEnrollInVM(WorkEnrollInVM workEnrollInVM) {
        this.workEnrollInVM = workEnrollInVM;
    }

    public Long getCurrUserId() {
        return currUserId;
    }

    public void setCurrUserId(Long currUserId) {
        this.currUserId = currUserId;
    }
}
