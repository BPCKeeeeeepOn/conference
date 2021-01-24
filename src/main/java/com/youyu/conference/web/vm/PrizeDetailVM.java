package com.youyu.conference.web.vm;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.time.LocalDateTime;

import static com.youyu.conference.common.ConferenceConstants.FULL_DATE_TIME;

@Data
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class PrizeDetailVM {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long userId;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String userName;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String userNumber;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long prizeId;

    private Integer prizeType;

    private String prizeName;


    @JsonFormat(pattern = FULL_DATE_TIME)
    private LocalDateTime createdTime;
}
