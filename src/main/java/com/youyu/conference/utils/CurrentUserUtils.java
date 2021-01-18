package com.youyu.conference.utils;

import com.youyu.conference.entity.CustomUser;
import com.youyu.conference.service.cache.UserInfoCacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class CurrentUserUtils {


    @Autowired
    private UserInfoCacheService userInfoCacheService;

    private CurrentUserUtils() {
    }

    /**
     * 获取当前登录用户的用户名
     *
     * @return
     */
    public String getCurrentLoginUserNumber() {
        String userNumber = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        /*if (Objects.isNull(customUser)) {
            throw new BizException(ResponseResult.fail(ResultCode.INVALID_AUTHTOKEN));
        }*/
        return userNumber;
    }

    /**
     * 获取当前登录用户的userId
     *
     * @return
     */
    public long getCurrUserId() {
        return getCurrUser().getId();
    }

    /**
     * 获取前台当前登录用户的用户信息
     *
     * @return
     */
    public CustomUser getCurrUser() {
        return userInfoCacheService.getUserCache(getCurrentLoginUserNumber());
    }


}
