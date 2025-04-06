package com.scratchgame.config;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class WinCombinationConfig {
    @JsonProperty("reward_multiplier")
    private double rewardMultiplier;
    @JsonProperty("when")
    private String when;
    @JsonProperty("count")
    private Integer count;
    @JsonProperty("group")
    private String group;
    @JsonProperty("covered_areas")
    private List<List<String>> coveredAreas;
    public WinCombinationConfig() {}
}