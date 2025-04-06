package com.scratchgame.config;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

public class ConfigLoader {
    private final ObjectMapper objectMapper;

    public ConfigLoader() {
        this.objectMapper = new ObjectMapper();
    }

    public GameConfig loadConfig(Path configPath) throws IOException {
        // Check if the file exists as a regular file first
        if (Files.exists(configPath)) {
            return objectMapper.readValue(configPath.toFile(), GameConfig.class);
        }

        // If not found as a file, try to load from resources
        String fileName = configPath.getFileName().toString();
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(fileName)) {
            if (inputStream == null) {
                throw new IOException("Config file not found: " + fileName);
            }
            return objectMapper.readValue(inputStream, GameConfig.class);
        }
    }
}