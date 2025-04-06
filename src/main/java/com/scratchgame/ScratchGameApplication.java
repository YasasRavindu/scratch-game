package com.scratchgame;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.scratchgame.config.ConfigLoader;
import com.scratchgame.config.GameConfig;
import com.scratchgame.engine.GameEngine;
import com.scratchgame.model.GameResult;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ScratchGameApplication {
    public static void main(String[] args) {
        try {
            // Parse command line arguments
            CommandLineArgs commandLineArgs = parseArgs(args);
            if (commandLineArgs == null) {
                System.exit(1);
            }

            // Load configuration
            GameConfig gameConfig = loadConfig(commandLineArgs.configPath);

            // Create and run game engine
            GameEngine gameEngine = new GameEngine(gameConfig);
            GameResult result = gameEngine.play(commandLineArgs.bettingAmount);

            // Output result as JSON
            ObjectMapper mapper = new ObjectMapper();
            String jsonOutput = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(result);
            System.out.println(jsonOutput);

        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            System.exit(1);
        }
    }

    private static CommandLineArgs parseArgs(String[] args) {
        String configPath = null;
        double bettingAmount = 0;

        for (int i = 0; i < args.length; i++) {
            if ("--config".equals(args[i]) && i + 1 < args.length) {
                configPath = args[++i];
            } else if ("--betting-amount".equals(args[i]) && i + 1 < args.length) {
                try {
                    bettingAmount = Double.parseDouble(args[++i]);
                } catch (NumberFormatException e) {
                    System.err.println("Error: Betting amount must be a number");
                    return null;
                }
            }
        }

        if (configPath == null) {
            System.err.println("Error: Config file path is required (--config)");
            return null;
        }

        if (bettingAmount <= 0) {
            System.err.println("Error: Betting amount must be positive (--betting-amount)");
            return null;
        }

        return new CommandLineArgs(configPath, bettingAmount);
    }


    private static GameConfig loadConfig(String configPath) throws IOException {
        Path path = Paths.get(configPath);
        ConfigLoader configLoader = new ConfigLoader();
        return configLoader.loadConfig(path);
    }

    private record CommandLineArgs(String configPath, double bettingAmount) {
    }


}