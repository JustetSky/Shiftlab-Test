package com.justet.shiftlabtest.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.justet.shiftlabtest.api.ApiPath;
import com.justet.shiftlabtest.api.model.PageResponse;
import com.justet.shiftlabtest.api.model.analytic.*;
import com.justet.shiftlabtest.core.service.AnalyticsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class AnalyticsControllerTest {

    private MockMvc mockMvc;

    @Mock
    private AnalyticsService analyticsService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        AnalyticsController controller =
                new AnalyticsController(analyticsService);

        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .build();

        objectMapper.findAndRegisterModules();
    }

    @Test
    void getMostProductive_shouldReturnResult() throws Exception {

        MostProductiveResponse response = MostProductiveResponse.builder()
                .sellerId(1L)
                .totalAmount(BigDecimal.valueOf(1000))
                .build();

        when(analyticsService.getMostProductive(PeriodType.DAY))
                .thenReturn(response);

        mockMvc.perform(get(ApiPath.ANALYTICS + "/most-productive/{period}", "DAY"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.sellerId").value(1))
                .andExpect(jsonPath("$.totalAmount").value(1000));

        verify(analyticsService).getMostProductive(PeriodType.DAY);
    }

    @Test
    void getSellersWithTotalLessThan_shouldReturnPage() throws Exception {

        SellerTotalResponse seller = SellerTotalResponse.builder()
                .sellerId(1L)
                .totalAmount(BigDecimal.valueOf(50))
                .build();

        PageResponse<SellerTotalResponse> page = PageResponse.<SellerTotalResponse>builder()
                .items(List.of(seller))
                .page(0)
                .size(1)
                .total(1)
                .hasNext(false)
                .build();

        when(analyticsService.getSellersWithTotalLessThan(
                eq(PeriodType.MONTH),
                eq(BigDecimal.valueOf(100)),
                any()
        )).thenReturn(page);

        mockMvc.perform(get(ApiPath.ANALYTICS + "/sellers/total-less-than")
                        .param("period", "MONTH")
                        .param("amount", "100"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items[0].sellerId").value(1))
                .andExpect(jsonPath("$.items[0].totalAmount").value(50));

        verify(analyticsService)
                .getSellersWithTotalLessThan(eq(PeriodType.MONTH), eq(BigDecimal.valueOf(100)), any());
    }

    @Test
    void getBestPeriod_shouldReturnBestPeriod() throws Exception {

        LocalDateTime from = LocalDateTime.of(2026, 3, 1, 0, 0);
        LocalDateTime to = LocalDateTime.of(2026, 4, 1, 0, 0);

        BestPeriodResponse response = BestPeriodResponse.builder()
                .startDate(from)
                .endDate(to)
                .totalAmount(BigDecimal.valueOf(500))
                .build();

        when(analyticsService.getBestPeriod(
                eq(1L),
                eq(PeriodType.DAY),
                any(),
                any()
        )).thenReturn(response);

        mockMvc.perform(get(ApiPath.ANALYTICS + "/best-period/{sellerId}", 1)
                        .param("period", "DAY")
                        .param("from", "2026-03-01T00:00:00")
                        .param("to", "2026-04-01T00:00:00"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalAmount").value(500));

        verify(analyticsService)
                .getBestPeriod(eq(1L), eq(PeriodType.DAY), any(), any());
    }
}