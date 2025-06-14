package com.grup30.stickerapp.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class ConfigService {
    private final AppConfigSchema appConfig;

    public ConfigService() {
        this.appConfig = loadConfig();
    }

    private AppConfigSchema loadConfig() {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(
                    new ClassPathResource("app.config.json").getFile(),
                    AppConfigSchema.class
            );
        } catch (IOException e) {
            throw new RuntimeException("Failed to load app.config.json", e);
        }
    }

    public AppConfigSchema getAppConfig() {
        return appConfig;
    }
}
