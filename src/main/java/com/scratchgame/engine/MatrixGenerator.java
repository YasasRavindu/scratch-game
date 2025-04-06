package com.scratchgame.engine;

import com.scratchgame.config.BonusSymbolConfig;
import com.scratchgame.config.GameConfig;
import com.scratchgame.config.StandardSymbolConfig;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class MatrixGenerator {
    private final GameConfig config;
    private final Random random;

    public MatrixGenerator(GameConfig config) {
        this.config = config;
        this.random = new Random();
    }

    public String[][] generateMatrix() {
        int rows = config.getRows();
        int columns = config.getColumns();

        String[][] matrix = new String[rows][columns];

        // Generate standard symbols first
        Map<String, StandardSymbolConfig> probabilityByPosition = new HashMap<>();
        for (StandardSymbolConfig probabilityConfig : config.getProbabilities().getStandardSymbols()) {
            String key = probabilityConfig.getRow() + ":" + probabilityConfig.getColumn();
            probabilityByPosition.put(key, probabilityConfig);
        }

        // Fill the matrix with standard symbols
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < columns; col++) {
                String key = row + ":" + col;
                StandardSymbolConfig probabilityConfig = probabilityByPosition.get(key);

                // If no specific probability found for this position, use the first one as default
                if (probabilityConfig == null && !config.getProbabilities().getStandardSymbols().isEmpty()) {
                    probabilityConfig = config.getProbabilities().getStandardSymbols().getFirst();
                }

                if (probabilityConfig != null) {
                    matrix[row][col] = selectRandomSymbol(probabilityConfig.getSymbols());
                }
            }
        }

        // Randomly place bonus symbols in the matrix
        placeBonusSymbols(matrix);

        return matrix;
    }

    private void placeBonusSymbols(String[][] matrix) {
        BonusSymbolConfig bonusProbConfig = config.getProbabilities().getBonusSymbols();

        if (bonusProbConfig == null || bonusProbConfig.getSymbols() == null || bonusProbConfig.getSymbols().isEmpty()) {
            return;
        }

        // Decide on a random position for a bonus symbol
        int rows = matrix.length;
        int cols = matrix[0].length;

        int randomRow = random.nextInt(rows);
        int randomCol = random.nextInt(cols);

        // Select a random bonus symbol according to probabilities
        String bonusSymbol = selectRandomSymbol(bonusProbConfig.getSymbols());

        // Place it in the matrix
        matrix[randomRow][randomCol] = bonusSymbol;
    }

    private String selectRandomSymbol(Map<String, Integer> symbolProbabilities) {
        int totalWeight = symbolProbabilities.values().stream().mapToInt(Integer::intValue).sum();
        int randomWeight = random.nextInt(totalWeight) + 1;

        int cumulativeWeight = 0;
        for (Map.Entry<String, Integer> entry : symbolProbabilities.entrySet()) {
            cumulativeWeight += entry.getValue();
            if (randomWeight <= cumulativeWeight) {
                return entry.getKey();
            }
        }

        // This should never happen if the probabilities are properly set up
        return symbolProbabilities.keySet().iterator().next();
    }
}