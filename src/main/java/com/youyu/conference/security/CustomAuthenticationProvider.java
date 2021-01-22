package com.youyu.conference.security;

import com.youyu.conference.entity.CustomUser;
import com.youyu.conference.jwt.JWTLoginUser;
import com.youyu.conference.repository.CustomUserMapper;
import com.youyu.conference.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * 自定义身份验证(姓名，工号，集群)
 */
@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    private UserService userService;

    @Autowired
    private CustomUserMapper customUserMapper;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String userNumber = authentication.getName();
        String userName = authentication.getCredentials().toString();
        CustomUser user = userService.selectUserByUserNumber(userNumber);
        if (Objects.nonNull(user) && StringUtils.equalsIgnoreCase(userName, user.getUserName())) {
            if (!user.getLoginState()) { //更新用户登录状态
                user.setLoginState(Boolean.TRUE);
                customUserMapper.updateByPrimaryKeySelective(user);
            }
            //这里设置权限和角色
            Collection<GrantedAuthority> authorities = obtionGrantedAuthorities(user);
            //生成令牌
            return new UsernamePasswordAuthenticationToken(userNumber, authorities);
        } else {
            throw new BadCredentialsException("密码错误");
        }
    }

    /**
     * 取得用户的权限
     *
     * @param user 用户信息对象
     * @return
     */
    private Set<GrantedAuthority> obtionGrantedAuthorities(CustomUser user) {

        Set<GrantedAuthority> authSet = new HashSet<GrantedAuthority>();
        authSet.add(new SimpleGrantedAuthority("1"));

        return authSet;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }

}
