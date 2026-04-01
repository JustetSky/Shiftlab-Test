package com.justet.shiftlabtest.core.service;

import com.justet.shiftlabtest.api.constant.PeriodType;
import com.justet.shiftlabtest.api.dto.PageResponse;
import com.justet.shiftlabtest.api.dto.analytic.BestPeriodResponse;
import com.justet.shiftlabtest.api.dto.analytic.MostProductiveResponse;
import com.justet.shiftlabtest.api.dto.analytic.SellerTotalResponse;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public interface AnalyticsService {

    MostProductiveResponse getMostProductive(PeriodType period);

    PageResponse<SellerTotalResponse> getSellersWithTotalLessThan(PeriodType period, BigDecimal amount, Pageable pageable);

    BestPeriodResponse getBestPeriod(Long sellerId, PeriodType period, LocalDateTime from, LocalDateTime to);
    
}