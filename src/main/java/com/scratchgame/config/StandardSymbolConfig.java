package com.scratchgame.config;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Map;

@Data
public class StandardSymbolConfig {
    @JsonProperty("column")
    private int column;
    @JsonProperty("row")
    private int row;
    @JsonProperty("symbols")
    private Map<String, Integer> symbols;

    public StandardSymbolConfig() {
    }


}