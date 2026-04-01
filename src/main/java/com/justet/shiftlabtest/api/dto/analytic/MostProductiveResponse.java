package com.justet.shiftlabtest.api.dto.analytic;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MostProductiveResponse {

    private Long sellerId;
    private String name;
    private BigDecimal totalAmount;
}