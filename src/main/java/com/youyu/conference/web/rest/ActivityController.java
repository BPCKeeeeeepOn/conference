package com.youyu.conference.web.rest;

import com.youyu.conference.common.ResponseResult;
import com.youyu.conference.service.ActivityService;
import com.youyu.conference.web.vm.WorkInVM;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/activity")
@Slf4j
public class ActivityController {

    @Autowired
    private ActivityService activityService;

    @GetMapping("/user")
    public ResponseResult getUserInfo() {
        return ResponseResult.success().body(activityService.getUserInfo());
    }

    @GetMapping("/billboard/score")
    public ResponseResult getScoreBillboard() {
        return ResponseResult.success().body(activityService.getScoreBillboard());
    }

    @PostMapping("/work/commit")
    public ResponseResult commitWork(@Validated @RequestBody WorkInVM workInVM) {
        activityService.commitWork(workInVM);
        return ResponseResult.success();
    }
}
