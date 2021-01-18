package com.youyu.conference.jwt;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.ToString;

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@ToString
public class JWTLoginUser {

    private String userName;

    private String userNumber;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserNumber() {
        return userNumber;
    }

    public void setUserNumber(String userNumber) {
        this.userNumber = userNumber;
    }
}
