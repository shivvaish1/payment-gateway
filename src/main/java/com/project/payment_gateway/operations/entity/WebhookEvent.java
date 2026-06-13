package com.project.payment_gateway.operations.entity;

import com.project.payment_gateway.common.enums.WebhookEventStatus;
import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "webhook_event")
public class WebhookEvent {

    @Id
    private UUID id;
    @Column(nullable = false)
    private UUID merchantId;
    @Column(nullable = false)
    private String eventType;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private Map<String, Objects> payload;
    @Column(nullable = false)
    private String targetUrl;
    @Column(nullable = false)
    private String signature;
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private WebhookEventStatus status;

    private Integer attempts=0;

    private LocalDateTime nextRetryAt;

    private LocalDateTime lastAttemptsAt;

    private Integer lastResponseCode;

    private String lastResponseBody;

    private LocalDateTime deliveredAt;

}
