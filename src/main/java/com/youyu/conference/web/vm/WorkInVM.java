package com.youyu.conference.web.vm;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.youyu.conference.common.GeneratorID;
import com.youyu.conference.entity.UserEnrollWork;
import lombok.Data;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.beans.BeanUtils;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import static com.youyu.conference.common.ConferenceConstants.WORK_TYPE.WORK_TYPE_1;
import static com.youyu.conference.common.ConferenceConstants.WORK_TYPE.WORK_TYPE_2;

@Data
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class WorkInVM {

    @NotBlank(message = "作品链接不能为空")
    private String workUrl;

    private String workHeadImg;

    @NotBlank(message = "作品描述不能为空")
    @Size(max = 100, min = 1, message = "作品描述最大字符不能超过{max}")
    private String workDesc;

    @Max(value = WORK_TYPE_2, message = "作品类型错误")
    @Min(value = WORK_TYPE_1, message = "作品类型错误")
    private Integer workType;

    public UserEnrollWork buildInsertRecord() {
        UserEnrollWork record = new UserEnrollWork();
        BeanUtils.copyProperties(this, record);
        return record;
    }
}
