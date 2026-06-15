package com.project.payment_gateway.merchant.service;

import com.project.payment_gateway.merchant.dto.request.CreateApiKeyRequest;
import com.project.payment_gateway.merchant.dto.response.ApiKeyCreateResponse;
import com.project.payment_gateway.merchant.dto.response.ApiKeyResponse;

import java.util.List;
import java.util.UUID;

public interface ApiKeyService {
    ApiKeyCreateResponse createApiKey(UUID merchantId, CreateApiKeyRequest request);

    List<ApiKeyResponse> listByMerchant(UUID merchantId);

    void revoke(UUID merchantId, UUID keyId);

    ApiKeyCreateResponse rotate(UUID merchantId, UUID keyId);
}
