package com.project.payment_gateway.payment.service;

import com.project.payment_gateway.payment.dto.request.CreateOrderRequest;
import com.project.payment_gateway.payment.dto.response.OrderResponse;

import java.util.UUID;

public interface OrderService {
    OrderResponse create(UUID merchantId, CreateOrderRequest request);
}
