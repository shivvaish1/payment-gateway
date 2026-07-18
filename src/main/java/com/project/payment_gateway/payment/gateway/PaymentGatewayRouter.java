package com.project.payment_gateway.payment.gateway;

import com.project.payment_gateway.common.enums.PaymentMethod;
import com.project.payment_gateway.payment.gateway.dto.PaymentRequest;
import com.project.payment_gateway.payment.gateway.dto.PaymentResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class PaymentGatewayRouter {

    private final Map<PaymentMethod, PaymentAdapter> adapters;

    public PaymentResult initiate(PaymentRequest request){
        PaymentAdapter adapter = adapters.get(request.method());
        if(adapter==null){
            throw new IllegalArgumentException("No payment adapter found for payment method: "+request.method());
        }
        return adapter.initiate(request);

    }

    public PaymentResult capture(PaymentMethod method, UUID paymentId) {
        PaymentAdapter adapter = adapters.get(method);
        if (adapter == null) {
            throw new IllegalArgumentException("No payment adapter registered for method: "+method);
        }
        return adapter.capture(paymentId);
    }
}
