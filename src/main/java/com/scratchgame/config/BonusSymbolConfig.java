package com.scratchgame.config;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Map;

@Data
public class BonusSymbolConfig {
    @JsonProperty("symbols")
    private Map<String, Integer> symbols;
    public BonusSymbolConfig() {}

}