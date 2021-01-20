package com.youyu.conference.web.vm;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class WorkEnrollListOutVM extends WorkEnrollInVM {

    @JsonSerialize(using = ToStringSerializer.class)
    private Long workId;

    @JsonSerialize(using = ToStringSerializer.class)
    private Long userId;

    private String userName;

    private Integer workPv;

    private Integer workLike;

    private Integer workVote;
}
