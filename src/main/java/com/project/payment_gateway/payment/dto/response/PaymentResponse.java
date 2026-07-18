package com.project.payment_gateway.payment.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.project.payment_gateway.common.entity.Money;
import com.project.payment_gateway.common.enums.PaymentMethod;
import com.project.payment_gateway.common.enums.PaymentStatus;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;
@JsonInclude(JsonInclude.Include.NON_NULL)
public record PaymentResponse(
        UUID id,
        UUID merchantId,
        UUID orderId,
        Money amount,
        PaymentStatus status,
        PaymentMethod method,
        Map<String, Object> methodDetails,
        String errorCode,
        String errorDescription,
        Long refundedAmountPaise,
        LocalDateTime capturedAt,
        LocalDateTime createdAt



        ) {
}
