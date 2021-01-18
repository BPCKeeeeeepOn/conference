package com.youyu.conference.web.rest;

import com.youyu.conference.common.ResponseResult;
import com.youyu.conference.service.cache.UserInfoCacheService;
import com.youyu.conference.utils.CurrentUserUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/sys")
@Slf4j
public class SystemController {

    @Autowired
    CurrentUserUtils currentUserUtils;

    @Autowired
    private UserInfoCacheService userInfoCacheService;

    @GetMapping("/health")
    public ResponseEntity health() {
        Map<String, Object> msg = new HashMap<>();
        msg.put("now", LocalDateTime.now());
        msg.put("time_millis", System.currentTimeMillis());

        return ResponseEntity.ok(msg);
    }

    @GetMapping("/getUser")
    public ResponseResult getUser() {
        return ResponseResult.success().body(currentUserUtils.getCurrUser().getId());
    }

    /**
     * 删除用户信息缓存
     *
     * @param key
     * @return
     */
    @PutMapping("/delUserCache/{key}")
    public ResponseResult delUserInfoCache(@PathVariable String key) {
        userInfoCacheService.removeCache(key);
        return ResponseResult.success();
    }
}
