package com.project.payment_gateway.payment.processor.strategy;

import com.project.payment_gateway.payment.processor.PaymentProcessor;
import com.project.payment_gateway.payment.processor.dto.PaymentProcessorRequest;
import com.project.payment_gateway.payment.processor.dto.PaymentProcessorResponse;

public class CardPaymentProcessor implements PaymentProcessor {
    @Override
    public PaymentProcessorResponse charge(PaymentProcessorRequest request) {
        return null;
    }
}
