package com.youyu.conference.web.rest;

import com.youyu.conference.common.ResponseResult;
import com.youyu.conference.service.ActivityService;
import com.youyu.conference.utils.CommonUtils;
import com.youyu.conference.web.vm.ScoreInVM;
import com.youyu.conference.web.vm.WorkEnrollInVM;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

import static com.youyu.conference.common.ConferenceConstants.DEFAULT_PAGE_OFFSET;
import static com.youyu.conference.common.ConferenceConstants.DEFAULT_PAGE_SIZE;

@RestController
@RequestMapping("/activity")
@Slf4j
public class ActivityController {

    @Autowired
    private ActivityService activityService;

    /**
     * 获取用户信息
     *
     * @return
     */
    @GetMapping("/user")
    public ResponseResult getUserInfo() {
        return ResponseResult.success().body(activityService.getUserInfo());
    }

    /**
     * 积分排行榜
     *
     * @return
     */
    @GetMapping("/billboard/score")
    public ResponseResult getScoreBillboard(@RequestParam(required = false, defaultValue = "" + DEFAULT_PAGE_OFFSET) int offset,
                                            @RequestParam(required = false, defaultValue = "" + DEFAULT_PAGE_SIZE) int limit) {
        return ResponseResult.success().body(activityService.getScoreBillboard(offset, limit));
    }

    /**
     * 我的积分排行
     *
     * @return
     */
    @GetMapping("/billboard/myRound")
    public ResponseResult getMyRound() {
        return ResponseResult.success().body(activityService.getMyBillboard());
    }

    /**
     * 查询作品列表
     *
     * @param type
     * @return
     */
    @GetMapping("/work/list/{type}")
    public ResponseResult listWork(@PathVariable("type") Integer type,
                                   @RequestParam(required = false, defaultValue = "" + DEFAULT_PAGE_OFFSET) int offset,
                                   @RequestParam(required = false, defaultValue = "" + DEFAULT_PAGE_SIZE) int limit) {
        return ResponseResult.success().body(activityService.getWorkList(type, offset, limit));
    }

    /**
     * 上传作品
     *
     * @param file
     * @return
     */
    @PostMapping("/work/upload")
    public ResponseResult uploadWork(@RequestParam("file") MultipartFile file) {
        return ResponseResult.success().body(CommonUtils.uploadFile(file));
    }

    /**
     * 提交作品
     *
     * @param workEnrollInVM
     * @return
     */
    @PostMapping("/work/commit")
    public ResponseResult commitWork(@Validated @RequestBody WorkEnrollInVM workEnrollInVM) {
        activityService.commitWork(workEnrollInVM);
        return ResponseResult.success();
    }

    /**
     * 作品阅读/点赞/投票
     *
     * @return
     */
    @PostMapping("/work/event/{workId}/{event}")
    public ResponseResult eventWork(@PathVariable("workId") Long workId,
                                    @PathVariable("event") Integer event) {
        activityService.recordEventWork(workId, event);
        return ResponseResult.success();
    }

    /**
     * 新增积分
     *
     * @return
     */
    @PostMapping("/score/add")
    public ResponseResult addScore(@RequestBody @Validated ScoreInVM scoreInVM) {
        activityService.addScore(scoreInVM);
        return ResponseResult.success();
    }

    /**
     * 智能客服
     *
     * @param message
     * @return
     */
    @GetMapping("/message/reply")
    public ResponseResult replyMessage(@RequestParam(required = false, name = "message") String message) {
        Map<String, String> result = new HashMap<>();
        switch (message) {
            case "积分":
                result.put("result", "以下是积分规则");
                break;
            default:
                result.put("result", "欢迎参加年会，这是默认回答");
        }
        return ResponseResult.success().body(result);
    }

}
