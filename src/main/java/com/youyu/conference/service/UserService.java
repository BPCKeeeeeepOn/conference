package com.youyu.conference.service;

import com.youyu.conference.common.GeneratorID;
import com.youyu.conference.entity.CustomUser;
import com.youyu.conference.entity.CustomUserExample;
import com.youyu.conference.repository.CustomUserMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Component
@Slf4j
public class UserService {

    @Autowired
    private CustomUserMapper customUserMapper;

    public CustomUser selectUserByUserNumber(String userNumber) {
        CustomUserExample example = new CustomUserExample();
        example.clear();
        example.createCriteria().andUserNumberEqualTo(userNumber).andIsDeletedEqualTo(false);
        List<CustomUser> customUsers = customUserMapper.selectByExample(example);
        return CollectionUtils.isEmpty(customUsers) ? null : customUsers.get(NumberUtils.INTEGER_ZERO);
    }
}
