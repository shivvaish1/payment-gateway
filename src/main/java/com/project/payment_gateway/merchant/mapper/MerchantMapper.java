package com.project.payment_gateway.merchant.mapper;

import com.project.payment_gateway.merchant.dto.request.MerchantSignupRequest;
import com.project.payment_gateway.merchant.dto.response.MerchantResponse;
import com.project.payment_gateway.merchant.entity.Merchant;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface MerchantMapper {
    
    Merchant toEntityFromSignupRequest(MerchantSignupRequest request);

    MerchantResponse toResponse(Merchant merchant);
}
