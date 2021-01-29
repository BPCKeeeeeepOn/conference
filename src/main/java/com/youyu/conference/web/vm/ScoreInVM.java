package com.youyu.conference.web.vm;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import javax.validation.constraints.*;

@Data
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class ScoreInVM {

    @NotNull(message = "积分不能为空")
    @Max(value = 100, message = "积分不能大于100")
    @Min(value = 1, message = "积分不能小于1")
    private Integer score;

    @NotBlank(message = "渠道不能为空")
    @Size(max = 50, message = "渠道最大不超过{max}个字符")
    private String channel;
}
