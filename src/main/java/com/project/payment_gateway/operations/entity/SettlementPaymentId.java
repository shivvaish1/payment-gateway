package com.project.payment_gateway.operations.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.util.UUID;

@Embeddable

public class SettlementPaymentId {

    private UUID settlementId;


    private UUID paymentId;

}
