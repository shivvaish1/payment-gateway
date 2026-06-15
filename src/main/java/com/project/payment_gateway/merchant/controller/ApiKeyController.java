package com.project.payment_gateway.merchant.controller;

import com.project.payment_gateway.merchant.dto.request.CreateApiKeyRequest;
import com.project.payment_gateway.merchant.dto.response.ApiKeyCreateResponse;
import com.project.payment_gateway.merchant.dto.response.ApiKeyResponse;
import com.project.payment_gateway.merchant.service.ApiKeyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/v1/merchants/{merchantId}/api-keys")
@RequiredArgsConstructor
public class ApiKeyController {

    private final ApiKeyService apiKeyService;

    @PostMapping
    public ResponseEntity<ApiKeyCreateResponse> create(@PathVariable("merchantId") UUID merchantId,
                                                       @RequestBody CreateApiKeyRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(apiKeyService.createApiKey(merchantId, request));
    }

    @GetMapping
    public ResponseEntity<List<ApiKeyResponse>> listByMerchant(@PathVariable("merchantId") UUID merchantId
                                                         ) {
        return ResponseEntity.ok(apiKeyService.listByMerchant(merchantId));
    }

    @DeleteMapping("/{keyId}")
    public ResponseEntity<Void> revoke(@PathVariable UUID merchantId, @PathVariable UUID keyId) {
        apiKeyService.revoke(merchantId, keyId);
        // Implement the revoke logic here
        return ResponseEntity.noContent().build();

    }

    @PostMapping("/{keyId}/rotate")
    public ResponseEntity<ApiKeyCreateResponse> rotate(@PathVariable UUID merchantId,@PathVariable UUID keyId) {
        ApiKeyCreateResponse response = apiKeyService.rotate(merchantId, keyId);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }


}
