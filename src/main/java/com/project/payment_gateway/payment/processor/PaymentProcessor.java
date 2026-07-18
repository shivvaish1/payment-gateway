package com.project.payment_gateway.payment.processor;

import com.project.payment_gateway.payment.processor.dto.PaymentProcessorRequest;
import com.project.payment_gateway.payment.processor.dto.PaymentProcessorResponse;

public interface PaymentProcessor {

    PaymentProcessorResponse charge(PaymentProcessorRequest request);

}
