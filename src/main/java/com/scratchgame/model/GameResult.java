package com.scratchgame.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record GameResult(String[][] matrix, double reward,
                         @JsonProperty("applied_winning_combinations") Map<String, List<String>> appliedWinningCombinations,
                         @JsonProperty("applied_bonus_symbol") String appliedBonusSymbol) {
    public GameResult(String[][] matrix, double reward, Map<String, List<String>> appliedWinningCombinations, String appliedBonusSymbol) {
        this.matrix = matrix;
        this.reward = reward;
        this.appliedWinningCombinations = appliedWinningCombinations;
        this.appliedBonusSymbol = appliedBonusSymbol;
    }
}