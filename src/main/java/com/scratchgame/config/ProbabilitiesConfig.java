package com.scratchgame.config;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class ProbabilitiesConfig {

    @JsonProperty("standard_symbols")
    private List<StandardSymbolConfig> standardSymbols;
    @JsonProperty("bonus_symbols")
    private BonusSymbolConfig bonusSymbols;

    public ProbabilitiesConfig() {
    }

}
