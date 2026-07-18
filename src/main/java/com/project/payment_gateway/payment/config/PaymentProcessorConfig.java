package com.project.payment_gateway.payment.config;

import com.project.payment_gateway.common.enums.PaymentMethod;
import com.project.payment_gateway.payment.processor.PaymentProcessor;
import com.project.payment_gateway.payment.processor.strategy.CardPaymentProcessor;
import com.project.payment_gateway.payment.processor.strategy.NetBankingPaymentProcessor;
import com.project.payment_gateway.payment.processor.strategy.UpiPaymentProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;
@Configuration
public class PaymentProcessorConfig {
    @Bean
    public Map<PaymentMethod, PaymentProcessor> paymentProcessorMap(){
        return Map.of(
                PaymentMethod.CARD, new CardPaymentProcessor(),
                PaymentMethod.NETBANKING, new NetBankingPaymentProcessor(),
                PaymentMethod.UPI,new UpiPaymentProcessor()
        );
    }
}
