package com.justet.shiftlabtest.api.dto.transaction;

import com.justet.shiftlabtest.core.entity.PaymentType;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransactionResponse {

    private Long id;
    private Long sellerId;
    private BigDecimal amount;
    private PaymentType paymentType;
    private LocalDateTime transactionDate;
}