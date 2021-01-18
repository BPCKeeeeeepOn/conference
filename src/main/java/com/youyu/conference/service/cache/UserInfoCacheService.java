package com.youyu.conference.service.cache;

import com.youyu.conference.entity.CustomUser;
import com.youyu.conference.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

/**
 * 用户信息缓存
 *
 * @author USER
 */
@Component
public class UserInfoCacheService {

    @Autowired
    private UserService userService;

    /**
     * 获取前台用户缓存
     *
     * @param userNumber
     * @return
     */
    @Cacheable(cacheNames = "conference_user", key = "#userNumber")
    public CustomUser getUserCache(String userNumber) {
        CustomUser user = userService.selectUserByUserNumber(userNumber);
        return user;
    }


    /**
     * 删除前台用户缓存
     *
     * @param userNumber
     */
    @CacheEvict(cacheNames = "conference_user", key = "#userNumber")
    public void removeCache(String userNumber) {
        return;
    }

}
