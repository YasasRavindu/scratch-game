package com.scratchgame.engine.strategy;

import com.scratchgame.config.WinCombinationConfig;
import com.scratchgame.constant.GameConstant;
import com.scratchgame.model.Position;

import java.util.List;

public class SameSymbolsStrategy implements WinCombinationStrategy {
    @Override
    public boolean matches(String[][] matrix, String symbol, List<Position> positions, WinCombinationConfig config) {
        // For same symbols strategy, we just check if the count of positions meets the requirement
        return positions.size() >= config.getCount();
    }

    @Override
    public String getType() {
        return GameConstant.SAME_SYMBOLS;
    }
}
