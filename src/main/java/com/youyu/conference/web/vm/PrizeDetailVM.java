package com.youyu.conference.web.vm;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.time.LocalDateTime;

import static com.youyu.conference.common.ConferenceConstants.FULL_DATE_TIME;

@Data
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class PrizeDetailVM {

    private Integer prizeType;

    private String prizeName;

    @JsonFormat(pattern = FULL_DATE_TIME)
    private LocalDateTime createdTime;
}
