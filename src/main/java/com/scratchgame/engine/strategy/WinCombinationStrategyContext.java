package com.scratchgame.engine.strategy;

import java.util.HashMap;
import java.util.Map;

public class WinCombinationStrategyContext {
    private final Map<String, WinCombinationStrategy> strategies = new HashMap<>();

    public WinCombinationStrategyContext() {
        // Register all strategies
        addStrategy(new SameSymbolsStrategy());
        addStrategy(new LinearSymbolsStrategy());
        // Additional strategies can be added here
    }

    private void addStrategy(WinCombinationStrategy strategy) {
        strategies.put(strategy.getType(), strategy);
    }

    public WinCombinationStrategy getStrategy(String type) {
        return strategies.get(type);
    }

}
