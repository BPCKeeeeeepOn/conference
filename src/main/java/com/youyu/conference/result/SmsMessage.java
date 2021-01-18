package com.youyu.conference.result;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class SmsMessage {

    @JsonProperty("Message")
    private String message;

    @JsonProperty("RequestId")
    private String requestId;

    @JsonProperty("BizId")
    private String bizId;

    @JsonProperty("Code")
    private String code;

}
