package com.justet.shiftlabtest.core.service;

import com.justet.shiftlabtest.api.constant.PeriodType;
import com.justet.shiftlabtest.api.dto.analytic.MostProductiveResponse;

public interface AnalyticsService {

    MostProductiveResponse getMostProductive(PeriodType period);
}