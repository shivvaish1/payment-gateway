package com.project.payment_gateway.merchant.mapper;

import com.project.payment_gateway.merchant.dto.response.ApiKeyCreateResponse;
import com.project.payment_gateway.merchant.dto.response.ApiKeyResponse;
import com.project.payment_gateway.merchant.entity.ApiKey;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ApiKeyMapper {
    ApiKeyCreateResponse toApiKeyCreateResponse(String apiKey);

    List<ApiKeyResponse> toApiKeyResponseList(List<ApiKey> apiKeyList);
}
