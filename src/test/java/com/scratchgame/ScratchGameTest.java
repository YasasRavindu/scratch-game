package com.scratchgame;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.scratchgame.config.GameConfig;
import com.scratchgame.config.SymbolConfig;
import com.scratchgame.engine.GameEngine;
import com.scratchgame.engine.MatrixGenerator;
import com.scratchgame.engine.RewardCalculator;
import com.scratchgame.engine.WinningCombinationDetector;
import com.scratchgame.model.GameResult;
import com.scratchgame.model.Position;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ScratchGameTest {
    private GameEngine gameEngine;
    private GameConfig config;
    private MatrixGenerator mockMatrixGenerator;
    private WinningCombinationDetector mockWinningDetector;
    private RewardCalculator mockRewardCalculator;

    @BeforeEach
    void setUp() throws Exception {
        // 1. Load real config
        InputStream configStream = getClass().getResourceAsStream("/test-config.json");
        config = new ObjectMapper().readValue(configStream, GameConfig.class);

        // 2. Create mocks
        mockMatrixGenerator = Mockito.mock(MatrixGenerator.class);
        mockWinningDetector = Mockito.mock(WinningCombinationDetector.class);
        mockRewardCalculator = Mockito.mock(RewardCalculator.class);

        gameEngine = new GameEngine(
                config,
                mockMatrixGenerator,
                mockWinningDetector,
                mockRewardCalculator
        );
    }

    @Test
    void testPlay_WithWinningCombination() {
        // Setup mock matrix
        String[][] mockMatrix = {{"A", "B"}, {"A", "C"}};
        when(mockMatrixGenerator.generateMatrix()).thenReturn(mockMatrix);

        // Mock winning detection
        Map<String, List<String>> winningCombos = Map.of("A", List.of("same_symbol_3_times"));
        when(mockWinningDetector.detectWinningCombinations(any(), any())).thenReturn(winningCombos);

        // Mock reward calculation
        when(mockRewardCalculator.calculateBaseReward(10.0, winningCombos)).thenReturn(25.0);

        // Execute
        GameResult result = gameEngine.play(10.0);

        // Verify
        assertEquals(25.0, result.reward());
        assertArrayEquals(mockMatrix, result.matrix());
        verify(mockMatrixGenerator).generateMatrix();
    }

    @Test
    void testFindAllSymbolPositions() {
        // Directly test helper method
        String[][] matrix = {{"A", "B"}, {"A", "MISS"}};

        SymbolConfig symbolConfig1 = new SymbolConfig();
        symbolConfig1.setType("standard");
        symbolConfig1.setRewardMultiplier(1.0);
        symbolConfig1.setImpact("multiply_reward");

        SymbolConfig symbolConfig2 = new SymbolConfig();
        symbolConfig2.setType("standard");
        symbolConfig2.setRewardMultiplier(2.0);
        symbolConfig2.setImpact("multiply_reward");

        SymbolConfig symbolConfig3 = new SymbolConfig();
        symbolConfig3.setType("bonus");
        symbolConfig3.setRewardMultiplier(0.0);
        symbolConfig3.setImpact("extra_bonus");

        // Setup config symbols
        config.getSymbols().put("A", symbolConfig1);
        config.getSymbols().put("B", symbolConfig2);
        config.getSymbols().put("MISS", symbolConfig3);

        Map<String, List<Position>> positions = gameEngine.findAllSymbolPositions(matrix);

        assertEquals(2, positions.get("A").size());
        assertEquals(1, positions.get("B").size());
        assertNull(positions.get("MISS")); // Bonus symbols should be excluded
    }



}