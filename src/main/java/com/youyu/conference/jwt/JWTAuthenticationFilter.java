package com.youyu.conference.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.youyu.conference.common.ResponseResult;
import com.youyu.conference.common.ResultCode;
import com.youyu.conference.utils.RedisUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

@Slf4j
public class JWTAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private RedisUtils redisUtils;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {

        //从Header中拿到token
        String token = request.getHeader("Authorization");

        Authentication authentication = null;

        if (!Objects.isNull(token) && token.startsWith("Bearer ")) {
            try {
                authentication = JWTUtil.verifySign(token);
                //取出userNumber
                String userNumber = (String) authentication.getPrincipal();
                //对比缓存token
                String cacheToken = redisUtils.getCache(userNumber);

                if (!StringUtils.equals(cacheToken, token.replace(JWTUtil.TOKEN_PREFIX, "").trim())) {
                    throw new Exception();
                }

                refreshToken(token, authentication.getPrincipal().toString(), response);

            } catch (Exception e) {
                log.error("token无效: {}", e.getMessage());
                response.setCharacterEncoding("UTF-8");
                response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
                response.setStatus(HttpStatus.UNAUTHORIZED.value());

                response.getWriter().write(new ObjectMapper().writeValueAsString(ResponseResult.fail(ResultCode.INVALID_AUTHTOKEN)));
                return;
            }
        }

        SecurityContextHolder.getContext().setAuthentication(authentication);

        chain.doFilter(request, response);
    }

    /**
     * 刷新token
     *
     * @param oldToken 原始token
     * @param userName 用户手机号
     * @param response
     */
    public synchronized void refreshToken(String oldToken, String userName, HttpServletResponse response) {
        String cacheToken = null;
        try {
            cacheToken = redisUtils.getCache(userName);
        } catch (RedisConnectionFailureException e) {
            log.error("connection redis failed", e);
            return;
        }

        if (Objects.isNull(cacheToken)) {
            String newToken = JWTUtil.refreshToken(oldToken);

            if (!Objects.isNull(newToken)) {
                redisUtils.putCache(userName, newToken);
                response.setCharacterEncoding("UTF-8");
                response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
                response.setHeader("Access-Control-Expose-Headers", "Authentication-Info");
                response.setHeader("Authentication-Info", newToken);
            }
        }
    }
}
