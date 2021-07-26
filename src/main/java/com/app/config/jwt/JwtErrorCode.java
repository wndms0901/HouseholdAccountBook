package com.app.config.jwt;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum JwtErrorCode {
    EXPIRED_TOKEN(401, "expired", "expired token"),
    INVALID_TOKEN(401, "invalid", "invalid token"),
    NULL_TOKEN(401, "null", "null token");

    private final int status;
    private final String code;
    private final String message;
}