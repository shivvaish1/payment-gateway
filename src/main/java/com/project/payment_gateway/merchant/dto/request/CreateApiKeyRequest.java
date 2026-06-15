package com.project.payment_gateway.merchant.dto.request;

import com.project.payment_gateway.common.enums.Environment;

public record CreateApiKeyRequest(
        Environment environment
) {
}
