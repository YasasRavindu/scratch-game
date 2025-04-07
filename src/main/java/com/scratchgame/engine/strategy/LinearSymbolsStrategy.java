package com.scratchgame.engine.strategy;

import com.scratchgame.config.WinCombinationConfig;
import com.scratchgame.constant.GameConstant;
import com.scratchgame.model.Position;

import java.util.List;

public class LinearSymbolsStrategy implements WinCombinationStrategy {
    @Override
    public boolean matches(String[][] matrix, String symbol, List<Position> positions, WinCombinationConfig config) {
        if (config.getCoveredAreas() == null) {
            return false;
        }

        for (List<String> area : config.getCoveredAreas()) {
            List<Position> areaPositions = area.stream()
                    .map(Position::fromString)
                    .toList();

            if (positionsMatchSymbol(matrix, areaPositions, symbol)) {
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

    @Override
    public String getType() {
        return GameConstant.LINEAR_SYMBOLS;
    }
}
