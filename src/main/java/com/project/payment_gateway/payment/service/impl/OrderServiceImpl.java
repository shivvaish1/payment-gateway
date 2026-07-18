package com.project.payment_gateway.payment.service.impl;

import com.project.payment_gateway.common.enums.OrderStatus;
import com.project.payment_gateway.common.exception.BusinessRuleViolationException;
import com.project.payment_gateway.common.exception.DuplicateResourceException;
import com.project.payment_gateway.common.exception.ResourceNotFoundException;
import com.project.payment_gateway.payment.dto.request.CreateOrderRequest;
import com.project.payment_gateway.payment.dto.response.OrderResponse;
import com.project.payment_gateway.payment.dto.response.PaymentResponse;
import com.project.payment_gateway.payment.entity.OrderRecord;
import com.project.payment_gateway.payment.entity.Payment;
import com.project.payment_gateway.payment.mapper.OrderMapper;
import com.project.payment_gateway.payment.mapper.PaymentMapper;
import com.project.payment_gateway.payment.repository.OrderRepository;
import com.project.payment_gateway.payment.repository.PaymentRepository;
import com.project.payment_gateway.payment.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final PaymentRepository paymentRepository;
    private final PaymentMapper paymentMapper;
    private final OrderMapper orderMapper;
//    @Value("${app.order.get_expiry_minutes}")
//    private Integer defaultOrderExpiryMinutes;

    @Override
    @Transactional
    public OrderResponse create(UUID merchantId, CreateOrderRequest request) {
        if(request.receipt()!=null&&orderRepository.existsByMerchantIdAndReceipt(merchantId,request.receipt())){
            throw new DuplicateResourceException("ORDER_RECEIPT_DUPLICATE","Order with receipt already exists: "+request.receipt());
        }
        OrderRecord order=OrderRecord.builder()
                .receipt(request.receipt())
                .amount(request.amount())
                .notes(request.notes())
                .merchantId(merchantId)
                .orderStatus(OrderStatus.CREATED)
                .expiresAt(request.expiresAt()!=null?request.expiresAt(): LocalDateTime.now().plusMinutes(30))
                .build();
        order=orderRepository.save(order);

// TODO:        publish kafka event about order creation

        return orderMapper.toResponse(order);
    }

    @Override
    public OrderResponse getById(UUID merchantId, UUID orderId) {


        OrderRecord order= orderRepository.findByIdAndMerchantId(orderId,merchantId)
                .orElseThrow(() -> new ResourceNotFoundException("Order",orderId));

        return orderMapper.toResponse(order);

    }

    @Override
    @Transactional
    public OrderResponse cancel(UUID merchantId, UUID orderId) {

        OrderRecord order= orderRepository.findByIdAndMerchantId(orderId,merchantId)
                .orElseThrow(() -> new ResourceNotFoundException("Order",orderId));
        if(order.getOrderStatus()==OrderStatus.CANCELLED||order.getOrderStatus()==OrderStatus.PAID){
            throw new BusinessRuleViolationException("ORDER_CANNOT_CANCEL",
                    "Order cannot be cancelled as it is already "+order.getOrderStatus());
        }

        order.setOrderStatus(OrderStatus.CANCELLED);
        orderRepository.save(order);

        return orderMapper.toResponse(order);

    }

    @Override
    public List<PaymentResponse> listPayments(UUID merchantId, UUID orderId) {

        OrderRecord order=orderRepository.findByIdAndMerchantId(orderId,merchantId)
                .orElseThrow(() -> new ResourceNotFoundException("Order",orderId));

        List<Payment> paymentList= paymentRepository.findByOrder_Id(orderId);

//        return paymentList.stream()
//                .map(paymentMapper::toResponse)
//                .collect(Collectors.toList());

        return paymentMapper.toResponseList(paymentList);
    }
}
