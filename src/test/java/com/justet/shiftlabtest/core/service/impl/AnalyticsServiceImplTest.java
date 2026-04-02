package com.justet.shiftlabtest.core.service.impl;

import com.justet.shiftlabtest.api.model.PageResponse;
import com.justet.shiftlabtest.api.model.analytic.*;
import com.justet.shiftlabtest.core.entity.Seller;
import com.justet.shiftlabtest.core.exception.ServiceException;
import com.justet.shiftlabtest.core.repository.SellerRepository;
import com.justet.shiftlabtest.core.repository.TransactionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AnalyticsServiceImplTest {

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private SellerRepository sellerRepository;

    @InjectMocks
    private AnalyticsServiceImpl service;

    private Seller seller(Long id) {
        return Seller.builder()
                .id(id)
                .name("Seller " + id)
                .build();
    }

    @Test
    void getMostProductive_shouldReturnSeller() {

        Object[] row = new Object[]{1L, new BigDecimal("500")};

        when(transactionRepository.findTopSellerByPeriod(any(), any(), any()))
                .thenReturn(new PageImpl<>(java.util.Collections.singletonList(row)));

        when(sellerRepository.findById(1L))
                .thenReturn(Optional.of(seller(1L)));

        MostProductiveResponse result =
                service.getMostProductive(PeriodType.MONTH);

        assertThat(result.getSellerId()).isEqualTo(1L);
        assertThat(result.getTotalAmount()).isEqualByComparingTo("500");
    }

    @Test
    void getMostProductive_whenEmpty_shouldThrow() {

        when(transactionRepository.findTopSellerByPeriod(any(), any(), any()))
                .thenReturn(Page.empty());

        assertThatThrownBy(() ->
                service.getMostProductive(PeriodType.MONTH))
                .isInstanceOf(ServiceException.class)
                .hasMessageContaining("No transactions");
    }

    @Test
    void getSellersWithTotalLessThan_shouldReturnList() {

        Object[] row = new Object[]{1L, new BigDecimal("50")};

        when(transactionRepository.findSellersWithTotalLessThan(any(), any(), any(), any()))
                .thenReturn(new PageImpl<>(java.util.Collections.singletonList(row)));

        when(sellerRepository.findById(1L))
                .thenReturn(Optional.of(seller(1L)));

        PageResponse<SellerTotalResponse> result =
                service.getSellersWithTotalLessThan(
                        PeriodType.MONTH,
                        new BigDecimal("100"),
                        PageRequest.of(0, 10)
                );

        assertThat(result.getItems()).hasSize(1);
        assertThat(result.getItems().get(0).getSellerId()).isEqualTo(1L);
    }

    @Test
    void getBestPeriod_shouldReturnCorrectPeriod() {

        Object[] row = new Object[]{
                LocalDateTime.of(2026, 3, 1, 0, 0),
                new BigDecimal("300")
        };

        when(sellerRepository.findById(1L))
                .thenReturn(Optional.of(seller(1L)));

        when(transactionRepository.findBestMonth(any(), any(), any(), any()))
                .thenReturn(new PageImpl<>(java.util.Collections.singletonList(row)));

        BestPeriodResponse result = service.getBestPeriod(
                1L,
                PeriodType.MONTH,
                LocalDateTime.of(2026, 1, 1, 0, 0),
                LocalDateTime.of(2026, 12, 31, 0, 0)
        );

        assertThat(result.getTotalAmount()).isEqualByComparingTo("300");
        assertThat(result.getStartDate().getMonthValue()).isEqualTo(3);
    }

    @Test
    void getBestPeriod_whenSellerNotFound_shouldThrow() {

        when(sellerRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() ->
                service.getBestPeriod(
                        1L,
                        PeriodType.MONTH,
                        LocalDateTime.now(),
                        LocalDateTime.now()
                ))
                .isInstanceOf(ServiceException.class)
                .hasMessageContaining("not found");
    }

    @Test
    void getBestPeriod_whenFromAfterTo_shouldThrow() {

        when(sellerRepository.findById(1L))
                .thenReturn(Optional.of(seller(1L)));

        assertThatThrownBy(() ->
                service.getBestPeriod(
                        1L,
                        PeriodType.MONTH,
                        LocalDateTime.now().plusDays(1),
                        LocalDateTime.now()
                ))
                .isInstanceOf(ServiceException.class)
                .hasMessageContaining("from must be before to");
    }

    @Test
    void getBestPeriod_whenEmpty_shouldThrow() {

        when(sellerRepository.findById(1L))
                .thenReturn(Optional.of(seller(1L)));

        when(transactionRepository.findBestMonth(any(), any(), any(), any()))
                .thenReturn(Page.empty());

        assertThatThrownBy(() ->
                service.getBestPeriod(
                        1L,
                        PeriodType.MONTH,
                        LocalDateTime.now(),
                        LocalDateTime.now()
                ))
                .isInstanceOf(ServiceException.class)
                .hasMessageContaining("No transactions");
    }
}