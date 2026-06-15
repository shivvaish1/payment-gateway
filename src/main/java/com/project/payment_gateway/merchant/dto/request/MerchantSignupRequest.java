package com.project.payment_gateway.merchant.dto.request;

import com.project.payment_gateway.common.enums.BusinessType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record MerchantSignupRequest(

        @NotNull(message = "Name is required")
        @Size(min = 3, max = 50, message = "Name must be between 3 and 50 characters")
        String name,
        @Email(message = "Email should be valid")
        @NotNull(message = "Email is required")
        String email,

        @NotNull(message = "Password is required")
        @Size(min = 8, message = "Password should be at least 8 character long")
        String password,

        @Size(max = 100, message = "Business name should not be more than 50 characters long")
        String businessName,

        BusinessType businessType
) {
}
