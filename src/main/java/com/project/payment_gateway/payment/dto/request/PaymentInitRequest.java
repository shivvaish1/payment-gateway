package com.project.payment_gateway.payment.dto.request;

import com.project.payment_gateway.common.enums.PaymentMethod;
import com.project.payment_gateway.payment.entity.Payment;
import jakarta.validation.constraints.NotNull;

import java.util.Map;
import java.util.UUID;

public record PaymentInitRequest(
        @NotNull(message = "order id is required")
        UUID orderId,
        @NotNull(message = "payment method is required")
        PaymentMethod method,

        Map<String, Object> methodDetails
) {
}
