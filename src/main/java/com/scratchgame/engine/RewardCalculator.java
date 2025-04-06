package com.scratchgame.engine;

import com.scratchgame.config.GameConfig;
import com.scratchgame.config.SymbolConfig;
import com.scratchgame.config.WinCombinationConfig;

import java.util.List;
import java.util.Map;

public class RewardCalculator {
    private final GameConfig config;

    public RewardCalculator(GameConfig config) {
        this.config = config;
    }

    public double calculateBaseReward(double bettingAmount, Map<String, List<String>> winningCombinations) {
        double totalReward = 0.0;

        // Calculate reward for each symbol with winning combinations
        for (Map.Entry<String, List<String>> entry : winningCombinations.entrySet()) {
            String symbolName = entry.getKey();
            List<String> combinations = entry.getValue();

            SymbolConfig symbolConfig = config.getSymbols().get(symbolName);
            if (symbolConfig == null) continue;

            double symbolMultiplier = symbolConfig.getRewardMultiplier();
            double combinationsMultiplier = 1.0;

            // Apply all winning combination multipliers for this symbol
            for (String combinationName : combinations) {
                WinCombinationConfig winCombConfig = config.getWinCombinations().get(combinationName);
                if (winCombConfig != null) {
                    combinationsMultiplier *= winCombConfig.getRewardMultiplier();
                }
            }

            // Calculate the reward for this symbol
            double symbolReward = bettingAmount * symbolMultiplier * combinationsMultiplier;
            totalReward += symbolReward;
        }
        return totalReward;
    }

    public double applyBonusSymbol(double baseReward, String bonusSymbol) {
        SymbolConfig bonusConfig = config.getSymbols().get(bonusSymbol);
        if (bonusConfig == null || !bonusConfig.isBonusSymbol()) {
            return baseReward;
        }

        // Apply bonus based on its impact type
        return switch (bonusConfig.getImpact()) {
            case "multiply_reward" -> baseReward * bonusConfig.getRewardMultiplier();
            case "extra_bonus" -> baseReward + bonusConfig.getExtra();
            default -> baseReward;
        };
    }
}