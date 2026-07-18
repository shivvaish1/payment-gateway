package com.project.payment_gateway.payment.service.impl;

import com.project.payment_gateway.common.enums.OrderStatus;
import com.project.payment_gateway.common.enums.PaymentEvent;
import com.project.payment_gateway.common.enums.PaymentStatus;
import com.project.payment_gateway.common.exception.BusinessRuleViolationException;
import com.project.payment_gateway.common.exception.ResourceNotFoundException;
import com.project.payment_gateway.payment.dto.request.PaymentInitRequest;
import com.project.payment_gateway.payment.dto.response.PaymentResponse;
import com.project.payment_gateway.payment.entity.OrderRecord;
import com.project.payment_gateway.payment.entity.Payment;
import com.project.payment_gateway.payment.gateway.PaymentGatewayRouter;
import com.project.payment_gateway.payment.gateway.dto.PaymentRequest;
import com.project.payment_gateway.payment.gateway.dto.PaymentResult;
import com.project.payment_gateway.payment.mapper.PaymentMapper;
import com.project.payment_gateway.payment.repository.OrderRepository;
import com.project.payment_gateway.payment.repository.PaymentRepository;
import com.project.payment_gateway.payment.service.PaymentService;
import com.project.payment_gateway.payment.statemachine.PaymentTransitionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final OrderRepository orderRepository;
    private final PaymentRepository paymentRepository;
    private final PaymentGatewayRouter paymentGatewayRouter;
    private final PaymentMapper paymentMapper;
    private final PaymentTransitionService paymentTransitionService;

    @Override
    @Transactional(
//            isolation = Isolation.REPEATABLE_READ
    )
    public PaymentResponse initiate(UUID merchantId, PaymentInitRequest request) {

        OrderRecord order=orderRepository.findById(request.orderId())
                .orElseThrow(()->new ResourceNotFoundException("order",request.orderId()));

        if(!OrderStatus.CREATED.equals(order.getOrderStatus()) && !OrderStatus.ATTEMPTED.equals(order.getOrderStatus())){
            throw new BusinessRuleViolationException("ORDER_NOT_PAYABLE",
            "order cannot accept payment in status: "+order.getOrderStatus());
        }
        order.setOrderStatus(OrderStatus.ATTEMPTED);
        order.setAttempts(order.getAttempts()+1);

        Payment payment=Payment.builder()
                .order(order)
                .merchantId(merchantId)
                .amount(order.getAmount())
                .status(PaymentStatus.CREATED)
                .method(request.method())
                .methodDetails(request.methodDetails())
                .build();

        payment=paymentRepository.save(payment);

        PaymentRequest paymentRequest=new PaymentRequest(
                payment.getId(),
                request.orderId(),
                merchantId,
                order.getAmount(),
                request.method(),
                request.methodDetails());

        PaymentResult result=paymentGatewayRouter.initiate(paymentRequest);

        switch (result) {
            case PaymentResult.Pending pending -> payment.setProcessorReference(pending.registrationRef());
            case PaymentResult.Failure failure -> {
//                payment.setStatus(PaymentStatus.FAILED);
                payment.setErrorCode(failure.errorCode());
                payment.setErrorDescription(failure.errorDescription());
            }
            case PaymentResult.Success success -> {

            }
        }
        payment=paymentRepository.save(payment);
        orderRepository.save(order);

        // TODO: send an outbox (kafka event)

        return paymentMapper.toResponse(payment);
    }

    @Override
    public PaymentResponse capture(UUID merchantId, UUID paymentId) {

        Payment payment = paymentRepository.findByIdAndMerchantId(paymentId, merchantId)
                .orElseThrow(() -> new ResourceNotFoundException("Payment", paymentId));

        paymentTransitionService.apply(payment, PaymentEvent.CAPTURE_REQUEST);

        PaymentResult paymentResult = paymentGatewayRouter.capture(payment.getMethod(), paymentId);

        if(paymentResult instanceof  PaymentResult.Success success) {
            paymentTransitionService.apply(payment, PaymentEvent.CAPTURE_SUCCESS);
            payment.setCapturedAt(LocalDateTime.now());
            log.info("Payment captured, paymentID: {}", paymentId);
        } else if(paymentResult instanceof  PaymentResult.Failure failure) {
            paymentTransitionService.apply(payment, PaymentEvent.CAPTURE_FAIL);
            payment.setErrorCode(failure.errorCode());
            payment.setErrorDescription(failure.errorDescription());
            log.warn("Payment capture failed, paymentID: {}", paymentId);
        }

        payment = paymentRepository.save(payment);

//        TODO: send an outbox (kafka event)

        return paymentMapper.toResponse(payment);
    }
}
