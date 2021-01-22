package com.youyu.conference.web.vm;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class UserQueryParams {

    private String userName;

    private String userNumber;

    private String userCity;

    private Integer state;  //登录状态 0:未登录 1:已登录
}
