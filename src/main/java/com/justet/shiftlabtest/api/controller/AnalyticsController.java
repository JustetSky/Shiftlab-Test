package com.justet.shiftlabtest.api.controller;

import com.justet.shiftlabtest.api.constant.ApiPath;
import com.justet.shiftlabtest.api.constant.PeriodType;
import com.justet.shiftlabtest.api.dto.analytic.MostProductiveResponse;
import com.justet.shiftlabtest.core.service.AnalyticsService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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
}