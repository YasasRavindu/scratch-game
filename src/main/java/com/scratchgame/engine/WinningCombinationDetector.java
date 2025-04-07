package com.scratchgame.engine;

import com.scratchgame.config.GameConfig;
import com.scratchgame.config.WinCombinationConfig;
import com.scratchgame.engine.strategy.WinCombinationStrategy;
import com.scratchgame.engine.strategy.WinCombinationStrategyContext;
import com.scratchgame.model.Position;

import java.util.*;

public class WinningCombinationDetector {

    private final GameConfig config;
    private final WinCombinationStrategyContext strategyContext;

    public WinningCombinationDetector(GameConfig config) {
        this.config = config;
        this.strategyContext = new WinCombinationStrategyContext();
    }

    /**
     * @param matrix          : created matrix
     * @param symbolPositions : Map of symbol positioning
     * @return : winning combination map
     */
    public Map<String, List<String>> detectWinningCombinations(String[][] matrix, Map<String, List<Position>> symbolPositions) {
        Map<String, List<String>> appliedCombinations = new HashMap<>();
        Map<String, Set<String>> combinationGroups = new HashMap<>();

        for (Map.Entry<String, List<Position>> entry : symbolPositions.entrySet()) {
            String symbol = entry.getKey();
            List<Position> positions = entry.getValue();

            // Check each win combination
            for (Map.Entry<String, WinCombinationConfig> combinationEntry : config.getWinCombinations().entrySet()) {
                String combinationName = combinationEntry.getKey();
                WinCombinationConfig combinationConfig = combinationEntry.getValue();

                // Skip if this group already has a winning combination
                if (isGroupAlreadyUsed(combinationGroups, combinationConfig.getGroup(), combinationName)) {
                    continue;
                }

                // Get the appropriate strategy for this combination type
                WinCombinationStrategy strategy = strategyContext.getStrategy(combinationConfig.getWhen());

                // Skip if no strategy is found for this combination type
                if (strategy == null) {
                    continue;
                }

                // Check if the combination matches using the strategy
                if (strategy.matches(matrix, symbol, positions, combinationConfig)) {
                    applyCombination(appliedCombinations, combinationGroups, symbol, combinationName, combinationConfig.getGroup());
                }
            }
        }

        return appliedCombinations;
    }

    private boolean isGroupAlreadyUsed(Map<String, Set<String>> groups, String group, String combinationName) {
        return groups.containsKey(group) && groups.get(group).contains(combinationName);
    }

    private void applyCombination(
            Map<String, List<String>> appliedCombinations,
            Map<String, Set<String>> groups,
            String symbol,
            String combinationName,
            String group
    ) {
        appliedCombinations.computeIfAbsent(symbol, k -> new ArrayList<>()).add(combinationName);
        groups.computeIfAbsent(group, k -> new HashSet<>()).add(combinationName);
    }
}