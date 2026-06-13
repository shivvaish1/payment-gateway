package com.project.payment_gateway.operations.entity;

import com.project.payment_gateway.common.entity.Money;
import com.project.payment_gateway.common.enums.SettlementStatus;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "settlement")
public class Settlement {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column(nullable = false)
    private UUID merchantId;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "amountUnits", column = @Column(name = "gross_amount_units",nullable = false)),
            @AttributeOverride(name = "currency",column = @Column(name = "gross_amount_currency",nullable = false))
    })
    private Money grossAmount;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "amountUnits", column = @Column(name = "refund_amount_units",nullable = false)),
            @AttributeOverride(name = "currency",column = @Column(name = "refund_amount_currency",nullable = false))
    })
    private Money refudAmount;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "amountUnits", column = @Column(name = "gst_amount_units",nullable = false)),
            @AttributeOverride(name = "currency",column = @Column(name = "gst_amount_currency",nullable = false))
    })
    private Money gstAmount;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "amountUnits", column = @Column(name = "fee_amount_units",nullable = false)),
            @AttributeOverride(name = "currency",column = @Column(name = "fee_amount_currency",nullable = false))
    })
    private Money feeAmount;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "amountUnits", column = @Column(name = "net_amount_units",nullable = false)),
            @AttributeOverride(name = "currency",column = @Column(name = "net_amount_currency",nullable = false))
    })
    private Money netAmount;
    @Column(length = 100,nullable = false)
    private String bankReference;

    @Column(nullable = false,length = 20)
    private SettlementStatus status;

    private LocalDateTime processedAt;

}
