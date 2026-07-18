package com.project.payment_gateway.payment.service;

import com.project.payment_gateway.payment.dto.request.PaymentInitRequest;
import com.project.payment_gateway.payment.dto.response.PaymentResponse;

import java.util.UUID;

public interface PaymentService {

    PaymentResponse initiate(UUID merchantId, PaymentInitRequest request);

    PaymentResponse capture(UUID merchantId, UUID paymentId);

}
