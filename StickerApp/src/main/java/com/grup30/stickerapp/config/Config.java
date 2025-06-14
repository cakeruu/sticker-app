package com.grup30.stickerapp.config;

public record Config(
        Integer period,
        Integer maxAge,
        Integer token_exp
) { }
