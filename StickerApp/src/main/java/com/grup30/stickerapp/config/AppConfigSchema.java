package com.grup30.stickerapp.config;

public record AppConfigSchema(
    Config defaultConfig,
    Config production
) {}
