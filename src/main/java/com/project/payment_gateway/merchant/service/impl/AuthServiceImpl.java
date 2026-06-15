package com.project.payment_gateway.merchant.service.impl;

import com.project.payment_gateway.common.enums.MerchantStatus;
import com.project.payment_gateway.common.enums.UserRole;
import com.project.payment_gateway.common.exception.DuplicateResourceException;
import com.project.payment_gateway.merchant.dto.request.MerchantSignupRequest;
import com.project.payment_gateway.merchant.dto.response.MerchantResponse;
import com.project.payment_gateway.merchant.entity.AppUser;
import com.project.payment_gateway.merchant.entity.Merchant;
import com.project.payment_gateway.merchant.repository.AppUserRepository;
import com.project.payment_gateway.merchant.repository.MerchantRepository;
import com.project.payment_gateway.merchant.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {
    private final AppUserRepository appUserRepository;
    private final MerchantRepository merchantRepository;

    @Override
    @Transactional
    public MerchantResponse signup(MerchantSignupRequest request) {

        if(merchantRepository.existsByEmail(request.email())){
            throw new DuplicateResourceException("DUPLICATE_MERCHANT_EMAIL",
                    "Merchant with email already exists: "+request.email());
        }

        Merchant merchant = Merchant.builder()
                .name(request.name())
                .businessName(request.businessName())
                .businessType(request.businessType())
                .email(request.email())
                .status(MerchantStatus.PENDING_KYC)
                .build();
        merchant=merchantRepository.save(merchant);

        AppUser appUser=AppUser.builder()
                .email(request.email())
                .merchant(merchant)
                .passwordHash(request.password())
                .role(UserRole.OWNER)
                .build();
        appUserRepository.save(appUser);

        return new MerchantResponse(
                merchant.getId(),
                merchant.getName(),
                merchant.getEmail(),
                merchant.getBusinessName(),
                merchant.getBusinessType(),
                merchant.getStatus());
    }
}
