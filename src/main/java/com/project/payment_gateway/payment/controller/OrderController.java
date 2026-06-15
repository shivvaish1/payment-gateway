package com.project.payment_gateway.payment.controller;

import com.project.payment_gateway.payment.dto.request.CreateOrderRequest;
import com.project.payment_gateway.payment.dto.response.OrderResponse;
import com.project.payment_gateway.payment.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping( "/v1/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    UUID merchantId=UUID.fromString("5a8bdf77-c22a-4285-8785-4ab80a30daae");

    @PostMapping
    public ResponseEntity<OrderResponse> create(@RequestBody @Valid CreateOrderRequest request){
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(orderService.create(merchantId,request));
    }
}
