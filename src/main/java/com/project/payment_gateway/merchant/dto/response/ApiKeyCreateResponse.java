package com.project.payment_gateway.merchant.dto.response;

import com.project.payment_gateway.common.enums.Environment;

import java.util.UUID;

public record ApiKeyCreateResponse(
        UUID id,
        String keyId,
        String keySecret,
        Environment environment

) {
}
