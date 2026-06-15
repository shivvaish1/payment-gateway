package com.project.payment_gateway.payment.dto.request;

import com.project.payment_gateway.common.entity.Money;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.Map;

public record CreateOrderRequest(
        @NotNull(message = "Amount is required")
        Money amount,

        String receipt,// order-id (only known to merchant)

        Map<String, Object> notes,

        LocalDateTime expiresAt

) {
}
