package com.project.payment_gateway.payment.processor;

import com.project.payment_gateway.common.enums.PaymentMethod;
import com.project.payment_gateway.payment.processor.dto.PaymentProcessorRequest;
import com.project.payment_gateway.payment.processor.dto.PaymentProcessorResponse;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.Map;
@Component
public class PaymentProcessorRouter {

    private Map<PaymentMethod, PaymentProcessor> paymentProcessors;

    public PaymentProcessorResponse charge(PaymentProcessorRequest request){
        PaymentProcessor processor=paymentProcessors.get(request.method());
        if(processor==null){
            throw new IllegalArgumentException("No payment processor found for payment method: "+request.method());
        }
        return processor.charge(request);
    }
}
