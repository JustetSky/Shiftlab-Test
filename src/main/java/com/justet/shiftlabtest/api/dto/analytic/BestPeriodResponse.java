package com.justet.shiftlabtest.api.dto.analytic;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BestPeriodResponse {

    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private BigDecimal totalAmount;
}