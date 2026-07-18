package com.project.payment_gateway.merchant.service.impl;

import com.project.payment_gateway.common.exception.ResourceConflictException;
import com.project.payment_gateway.common.exception.ResourceNotFoundException;
import com.project.payment_gateway.common.util.RandomizerUtil;
import com.project.payment_gateway.merchant.dto.request.CreateApiKeyRequest;
import com.project.payment_gateway.merchant.dto.response.ApiKeyCreateResponse;
import com.project.payment_gateway.merchant.dto.response.ApiKeyResponse;
import com.project.payment_gateway.merchant.entity.ApiKey;
import com.project.payment_gateway.merchant.entity.Merchant;
import com.project.payment_gateway.merchant.mapper.ApiKeyMapper;
import com.project.payment_gateway.merchant.repository.ApiKeyRepository;
import com.project.payment_gateway.merchant.repository.MerchantRepository;
import com.project.payment_gateway.merchant.service.ApiKeyService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ApiKeyServiceImpl implements ApiKeyService {
    private final MerchantRepository merchantRepository;
    private final ApiKeyRepository apiKeyRepository;
    private final ApiKeyMapper apiKeyMapper;


    @Override
    @Transactional
    public ApiKeyCreateResponse createApiKey(UUID merchantId, CreateApiKeyRequest request) {
        Merchant merchant = merchantRepository.findById(merchantId)
                .orElseThrow(() -> new ResourceNotFoundException("Merchant", merchantId));
        String keyId= "rzp_"+request.environment().name().toLowerCase()+"_"+ RandomizerUtil.randomBase64(24);
//        String keyId= "pytgwy_"+request.environment().name().toLowerCase()+"_"+ RandomizerUtil.randomBase64(24);
        String rawSecret = RandomizerUtil.randomBase64(40); // TODO: replace with cryptographic random hex

        ApiKey apiKey = ApiKey.builder()
                .merchant(merchant)
                .keyId(keyId)
                .keySecretHash(rawSecret) // TODO: encode with BcryptPasswordEncoder
                .environment(request.environment())
                .build();

        apiKey = apiKeyRepository.save(apiKey);

        return new ApiKeyCreateResponse(apiKey.getId(), keyId, rawSecret, request.environment());

    }

    @Override
    public List<ApiKeyResponse> listByMerchant(UUID merchantId) {
        return apiKeyMapper.toApiKeyResponseList(apiKeyRepository.findByMerchant_Id(merchantId));

    }

    @Override
    @Transactional
    public void revoke(UUID merchantId, UUID keyId) {
        ApiKey key=apiKeyRepository.findById(keyId)
                .filter(k->k.getMerchant().getId().equals(merchantId))
                .orElseThrow(() -> new ResourceNotFoundException("ApiKey", keyId));

        key.setEnabled(false);
        apiKeyRepository.save(key);

    }

    @Override
    @Transactional
    public ApiKeyCreateResponse rotate(UUID merchantId, UUID keyId) {
        ApiKey key=apiKeyRepository.findById(keyId)
                .filter(k->k.getMerchant().getId().equals(merchantId))
                .orElseThrow(() -> new ResourceNotFoundException("ApiKey", keyId));

        if(!key.isEnabled()){
            throw new ResourceConflictException("API_KEY_DISABLED","Disabled Api key cannot be rotated");
        }

        String newRawSecret = RandomizerUtil.randomBase64(40);
        key.setPreviousKeySecretHash(key.getKeySecretHash());
        key.setKeySecretHash(newRawSecret);
        key.setRotatedAt(LocalDateTime.now());
        key.setGracePeriodExpiresAt(LocalDateTime.now().plusHours(24));
        key=apiKeyRepository.save(key);


        return new ApiKeyCreateResponse(key.getId(), key.getKeyId(), newRawSecret, key.getEnvironment());
    }
}
