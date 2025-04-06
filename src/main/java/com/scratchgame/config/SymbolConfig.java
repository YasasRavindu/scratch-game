package com.scratchgame.config;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class SymbolConfig {
    @JsonProperty("reward_multiplier")
    private double rewardMultiplier;
    @JsonProperty("type")
    private String type;
    @JsonProperty("impact")
    private String impact;
    @JsonProperty("extra")
    private double extra;

    public SymbolConfig() {
    }

    public boolean isStandardSymbol() {
        return "standard".equals(type);
    }

    public boolean isBonusSymbol() {
        return "bonus".equals(type);
    }
}
