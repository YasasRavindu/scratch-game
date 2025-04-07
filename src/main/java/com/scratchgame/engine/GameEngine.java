package com.scratchgame.engine;

import com.scratchgame.config.GameConfig;
import com.scratchgame.config.SymbolConfig;
import com.scratchgame.model.GameResult;
import com.scratchgame.model.Position;

import java.util.*;

public class GameEngine {
    private final GameConfig config;
    private final MatrixGenerator matrixGenerator;
    private final WinningCombinationDetector winningCombinationDetector;
    private final RewardCalculator rewardCalculator;

    // Original constructor (calls this() with real implementations)
    public GameEngine(GameConfig config) {
        this(config,
                new MatrixGenerator(config),
                new WinningCombinationDetector(config),
                new RewardCalculator(config));
    }

    public GameEngine(
            GameConfig config,
            MatrixGenerator matrixGenerator,
            WinningCombinationDetector winningCombinationDetector,
            RewardCalculator rewardCalculator
    ) {
        this.config = config;
        this.matrixGenerator = matrixGenerator;
        this.winningCombinationDetector = winningCombinationDetector;
        this.rewardCalculator = rewardCalculator;
    }


    public GameResult play(double bettingAmount) {
        // Generate the matrix
        String[][] matrix = matrixGenerator.generateMatrix();

        // Find all standard symbols in the matrix
        Map<String, List<Position>> symbolPositions = findAllSymbolPositions(matrix);

        // Find all bonus symbols in the matrix
        List<String> bonusSymbols = findBonusSymbols(matrix);

        // Detect winning combinations
        Map<String, List<String>> winningCombinations = winningCombinationDetector.detectWinningCombinations(matrix, symbolPositions);

        // Calculate reward
        double reward = 0.0;
        String appliedBonusSymbol = null;

        if (!winningCombinations.isEmpty()) {
            reward = rewardCalculator.calculateBaseReward(bettingAmount, winningCombinations);

            // Apply bonus if there's a win
            if (!bonusSymbols.isEmpty()) {
                // Randomly select one bonus symbol to apply (as per the requirements)
                String bonusSymbol = bonusSymbols.get(new Random().nextInt(bonusSymbols.size()));
                reward = rewardCalculator.applyBonusSymbol(reward, bonusSymbol);
                appliedBonusSymbol = bonusSymbol;
            }
        }

        return new GameResult(matrix, reward, winningCombinations.isEmpty() ? null : winningCombinations, appliedBonusSymbol);
    }

    public Map<String, List<Position>> findAllSymbolPositions(String[][] matrix) {
        Map<String, List<Position>> positions = new HashMap<>();

        for (int row = 0; row < matrix.length; row++) {
            for (int col = 0; col < matrix[row].length; col++) {
                String symbolName = matrix[row][col];
                SymbolConfig symbolConfig = config.getSymbols().get(symbolName);

                // Only track standard symbols
                if (symbolConfig != null && symbolConfig.isStandardSymbol()) {
                    positions.computeIfAbsent(symbolName, k -> new ArrayList<>())
                            .add(new Position(row, col));
                }
            }
        }

        return positions;
    }

    public List<String> findBonusSymbols(String[][] matrix) {
        List<String> bonusSymbols = new ArrayList<>();

        for (String[] strings : matrix) {
            for (String symbolName : strings) {
                SymbolConfig symbolConfig = config.getSymbols().get(symbolName);
                if (symbolConfig != null && symbolConfig.isBonusSymbol()) {
                    bonusSymbols.add(symbolName);
                }
            }
        }

        return bonusSymbols;
    }
}