package com.scratchgame.config;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Map;

@Data
public class GameConfig {
    @JsonProperty("columns")
    private int columns;
    @JsonProperty("rows")
    private int rows;
    @JsonProperty("symbols")
    private Map<String, SymbolConfig> symbols;
    @JsonProperty("probabilities")
    private ProbabilitiesConfig probabilities;
    @JsonProperty("win_combinations")
    private Map<String, WinCombinationConfig> winCombinations;

    public GameConfig() {
    }
}