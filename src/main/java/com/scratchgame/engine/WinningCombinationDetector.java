package com.scratchgame.engine;

import com.scratchgame.config.GameConfig;
import com.scratchgame.config.WinCombinationConfig;
import com.scratchgame.constant.GameConstant;
import com.scratchgame.model.Position;

import java.util.*;

public class WinningCombinationDetector {
    private final GameConfig config;

    public WinningCombinationDetector(GameConfig config) {
        this.config = config;
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
            int symbolCount = positions.size();

            // Check each win combination
            for (Map.Entry<String, WinCombinationConfig> combinationEntry : config.getWinCombinations().entrySet()) {
                String combinationName = combinationEntry.getKey();
                WinCombinationConfig config = combinationEntry.getValue();

                // Skip if this group already has a winning combination
                if (isGroupAlreadyUsed(combinationGroups, config.getGroup(), combinationName)) {
                    continue;
                }

                // Check for same symbol combinations
                if (isSameSymbolCombination(config, symbolCount)) {
                    // Add to applied combinations
                    applyCombination(appliedCombinations, combinationGroups, symbol, combinationName, config.getGroup());
                } else if (isLinearCombination(config)) {
                    // Check each covered area pattern
                    if (matchesAnyCoveredArea(matrix, config.getCoveredAreas(), symbol)) {
                        applyCombination(appliedCombinations, combinationGroups, symbol, combinationName, config.getGroup());
                    }
                }
            }
        }

        return appliedCombinations;
    }

    private boolean isGroupAlreadyUsed(Map<String, Set<String>> groups, String group, String combinationName) {
        return groups.containsKey(group) && groups.get(group).contains(combinationName);
    }

    private boolean isSameSymbolCombination(WinCombinationConfig config, int symbolCount) {
        return GameConstant.SAME_SYMBOLS.equals(config.getWhen()) && symbolCount >= config.getCount();
    }

    private boolean isLinearCombination(WinCombinationConfig config) {
        return GameConstant.LINEAR_SYMBOLS.equals(config.getWhen()) && config.getCoveredAreas() != null;
    }

    private boolean matchesAnyCoveredArea(String[][] matrix, List<List<String>> areas, String symbol) {
        for (List<String> area : areas) {
            List<Position> positions = area.stream()
                    .map(Position::fromString)
                    .toList();

            if (positionsMatchSymbol(matrix, positions, symbol)) {
                return true;
            }
        }
        return false;
    }

    private boolean positionsMatchSymbol(String[][] matrix, List<Position> positions, String symbol) {
        for (Position pos : positions) {
            if (!isWithinBounds(matrix, pos) || !symbol.equals(matrix[pos.getRow()][pos.getColumn()])) {
                return false;
            }
        }
        return true;
    }

    private boolean isWithinBounds(String[][] matrix, Position pos) {
        return pos.getRow() < matrix.length && pos.getColumn() < matrix[0].length;
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