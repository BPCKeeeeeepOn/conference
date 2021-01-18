package com.youyu.conference.web.vm;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class ScoreBillboardVM {

    @JsonSerialize(using = ToStringSerializer.class)
    private Long userId;

    private String userName;

    private String userNumber;

    private Integer score;

    private Integer rownum;
}
