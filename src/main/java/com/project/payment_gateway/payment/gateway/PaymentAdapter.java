package com.project.payment_gateway.payment.gateway;

import com.project.payment_gateway.payment.gateway.dto.PaymentRequest;
import com.project.payment_gateway.payment.gateway.dto.PaymentResult;

import java.util.UUID;

public interface PaymentAdapter {
    PaymentResult initiate(PaymentRequest request);

    PaymentResult capture(UUID paymentId);
}
