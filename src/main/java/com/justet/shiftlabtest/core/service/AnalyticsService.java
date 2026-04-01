package com.justet.shiftlabtest.core.service;

import com.justet.shiftlabtest.api.model.analytic.PeriodType;
import com.justet.shiftlabtest.api.model.PageResponse;
import com.justet.shiftlabtest.api.model.analytic.BestPeriodResponse;
import com.justet.shiftlabtest.api.model.analytic.MostProductiveResponse;
import com.justet.shiftlabtest.api.model.analytic.SellerTotalResponse;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public interface AnalyticsService {

    MostProductiveResponse getMostProductive(PeriodType period);

    PageResponse<SellerTotalResponse> getSellersWithTotalLessThan(PeriodType period, BigDecimal amount, Pageable pageable);

    BestPeriodResponse getBestPeriod(Long sellerId, PeriodType period, LocalDateTime from, LocalDateTime to);
    
}