# Scratch Game Simulator

A command-line Java application that simulates a scratch card game with configurable rules, win combinations, and bonus effects.

## Quick Start

### Prerequisites

-   Java 17+
-   Maven 3.8+

### Run the Game


# Build the project
mvn clean package

# Execute with your chosen config (examples below)
java -jar target/scratch-game-1.0.jar --config [CONFIG_FILE] --betting-amount [AMOUNT]


Test Scenarios
1. Default Configuration
Bash
java -jar target/scratch-game-1.0.jar --config config.json --betting-amount 100

2. Basic 3x3 Grid
Tests the base game rules with standard 3x3 grid.
Bash
java -jar target/scratch-game-1.0.jar --config config2.json --betting-amount 100

3. Linear Combinations
Standard symbols with simple win patterns.
Bash
java -jar target/scratch-game-1.0.jar --config config3.json --betting-amount 100

4. High Reward Multipliers
Tests horizontal/vertical line wins (configured in win_combinations).
Bash
java -jar target/scratch-game-1.0.jar --config config4.json --betting-amount 100

5. 4x4 Grid Size
Uses symbols with 10x, 20x multipliers and bonus rewards.
Bash
java -jar target/scratch-game-1.0.jar --config config5.json --betting-amount 100

6. Multiple Bonus Symbols
Validates non-standard matrix handling.
Bash
java -jar target/scratch-game-1.0.jar --config config6.json --betting-amount 100


Config Files
Place these in your project's root directory:

config.json - Base configuration
config2.json - Basic 3x3 grid
config3.json - Linear combinations
config4.json - High rewards
config5.json - 4x4 grid
config6.json - Multiple bonuses


Example config structure:

JSON

{
  "columns": 3,
  "rows": 3,
  "symbols": {
    "A": { "reward_multiplier": 1, "type": "standard" },
    "10x": { "reward_multiplier": 10, "type": "bonus" }
  },
  "win_combinations": {
    "same_symbol_3_times": {
      "reward_multiplier": 2,
      "when": "same_symbols",
      "count": 3
    }
  }
}


Development

Build & Run Tests
```bash
  mvn clean install
```

Expected Output Format
JSON

{
  "matrix": [["A", "B", "A"], ["C", "10x", "A"]],
  "reward": 200.0,
  "applied_winning_combinations": {
    "A": ["same_symbol_3_times"]
  },
  "applied_bonus_symbol": "10x"
}