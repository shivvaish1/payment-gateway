package com.project.payment_gateway.merchant.service;

import com.project.payment_gateway.merchant.dto.request.MerchantSignupRequest;
import com.project.payment_gateway.merchant.dto.response.MerchantResponse;
import jakarta.validation.Valid;

public interface AuthService {

    MerchantResponse signup(@Valid MerchantSignupRequest request);
}
