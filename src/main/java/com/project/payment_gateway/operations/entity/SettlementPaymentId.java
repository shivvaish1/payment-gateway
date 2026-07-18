package com.project.payment_gateway.operations.entity;

import com.project.payment_gateway.common.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.util.UUID;

@Embeddable

public class SettlementPaymentId extends BaseEntity {

    private UUID settlementId;


    private UUID paymentId;

}
