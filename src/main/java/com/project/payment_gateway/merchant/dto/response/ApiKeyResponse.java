package com.project.payment_gateway.merchant.dto.response;

import com.project.payment_gateway.common.enums.Environment;

import java.time.LocalDateTime;
import java.util.UUID;

public record ApiKeyResponse(
        UUID id,
        String keyId,
        Environment environment,
        boolean enabled,
        LocalDateTime getLastUsedAt,
        LocalDateTime createdAt
        ) {
}
