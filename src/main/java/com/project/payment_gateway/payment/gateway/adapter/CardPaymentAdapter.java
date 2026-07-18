package com.project.payment_gateway.payment.gateway.adapter;

import com.project.payment_gateway.payment.gateway.PaymentAdapter;
import com.project.payment_gateway.payment.gateway.dto.PaymentRequest;
import com.project.payment_gateway.payment.gateway.dto.PaymentResult;

import java.util.UUID;

public class CardPaymentAdapter implements PaymentAdapter {


    @Override
    public PaymentResult initiate(PaymentRequest request) {
        return null;
    }

    @Override
    public PaymentResult capture(UUID paymentId) {
        return null;
    }

}
