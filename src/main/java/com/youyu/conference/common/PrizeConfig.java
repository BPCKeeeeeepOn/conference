package com.youyu.conference.common;

import lombok.Data;

@Data
public class PrizeConfig {

    private Integer index;

    private String prizeKey;

    private Integer prizeType;

    private String prizeName;

    private Double probability; //中奖概率

    private Integer count; //奖品数量

    public PrizeConfig(Integer index, String prizeKey, Integer prizeType, String prizeName, Double probability, Integer count) {
        this.index = index;
        this.prizeKey = prizeKey;
        this.prizeType = prizeType;
        this.prizeName = prizeName;
        this.probability = probability;
        this.count = count;
    }

    public PrizeConfig() {
    }
}
