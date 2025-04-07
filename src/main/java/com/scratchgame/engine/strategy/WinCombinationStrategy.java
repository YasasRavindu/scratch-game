package com.scratchgame.engine.strategy;

import com.scratchgame.config.WinCombinationConfig;
import com.scratchgame.model.Position;

import java.util.List;

// Base strategy interface
public interface WinCombinationStrategy {
    /**
     * Checks if the given positions match the winning combination configuration
     *
     * @param matrix The game matrix
     * @param symbol The symbol to check
     * @param positions List of positions where the symbol appears
     * @param config The winning combination configuration
     * @return true if the combination matches, false otherwise
     */
    boolean matches(String[][] matrix, String symbol, List<Position> positions, WinCombinationConfig config);

    /**
     * Returns the type of combination this strategy handles
     */
    String getType();
}
