package com.justet.shiftlabtest.api.controller;

import com.justet.shiftlabtest.api.constant.ApiPath;
import com.justet.shiftlabtest.api.constant.PeriodType;
import com.justet.shiftlabtest.api.dto.PageResponse;
import com.justet.shiftlabtest.api.dto.analytic.BestPeriodResponse;
import com.justet.shiftlabtest.api.dto.analytic.MostProductiveResponse;
import com.justet.shiftlabtest.api.dto.analytic.SellerTotalResponse;
import com.justet.shiftlabtest.core.service.AnalyticsService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Validated
@RestController
@RequestMapping(ApiPath.ANALYTICS)
@RequiredArgsConstructor
public class AnalyticsController {

    private final AnalyticsService analyticsService;

    @Operation(summary = "Самый продуктивный продавец")
    @GetMapping(ApiPath.MOST_PRODUCTIVE)
    public MostProductiveResponse getMostProductive(
            @PathVariable PeriodType period
    ) {
        return analyticsService.getMostProductive(period);
    }

    @Operation(summary = "Продавцы с суммой меньше указанной")
    @GetMapping(ApiPath.SELLERS + ApiPath.TOTAL_LESS_THAN)
    public PageResponse<SellerTotalResponse> getSellersWithTotalLessThan(
            @RequestParam PeriodType period,
            @RequestParam BigDecimal amount,
            Pageable pageable
    ) {
        return analyticsService.getSellersWithTotalLessThan(period, amount, pageable);
    }

    @Operation(summary = "Лучший период продавца")
    @GetMapping(ApiPath.BEST_PERIOD + ApiPath.SELLER_ID)
    public BestPeriodResponse getBestPeriod(
            @PathVariable Long sellerId,
            @RequestParam PeriodType period,
            @RequestParam LocalDateTime from,
            @RequestParam LocalDateTime to
    ) {
        return analyticsService.getBestPeriod(sellerId, period, from, to);
    }
}