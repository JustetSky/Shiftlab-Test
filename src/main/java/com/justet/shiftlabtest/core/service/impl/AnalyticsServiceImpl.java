package com.justet.shiftlabtest.core.service.impl;

import com.justet.shiftlabtest.api.constant.PeriodType;
import com.justet.shiftlabtest.api.dto.PageResponse;
import com.justet.shiftlabtest.api.dto.analytic.BestPeriodResponse;
import com.justet.shiftlabtest.api.dto.analytic.MostProductiveResponse;
import com.justet.shiftlabtest.api.dto.analytic.SellerTotalResponse;
import com.justet.shiftlabtest.core.entity.Seller;
import com.justet.shiftlabtest.core.entity.Transaction;
import com.justet.shiftlabtest.core.exception.ErrorCode;
import com.justet.shiftlabtest.core.exception.ServiceException;
import com.justet.shiftlabtest.core.repository.SellerRepository;
import com.justet.shiftlabtest.core.repository.TransactionRepository;
import com.justet.shiftlabtest.core.service.AnalyticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AnalyticsServiceImpl implements AnalyticsService {

    private final TransactionRepository transactionRepository;
    private final SellerRepository sellerRepository;

    @Override
    public MostProductiveResponse getMostProductive(PeriodType period) {

        LocalDateTime now = LocalDateTime.now();

        LocalDateTime start = switch (period) {
            case DAY -> now.toLocalDate().atStartOfDay();
            case MONTH -> now.withDayOfMonth(1).toLocalDate().atStartOfDay();
            case YEAR -> now.withDayOfYear(1).toLocalDate().atStartOfDay();
            case QUARTER -> {
                int currentMonth = now.getMonthValue();
                int startMonth = ((currentMonth - 1) / 3) * 3 + 1;
                yield LocalDateTime.of(now.getYear(), startMonth, 1, 0, 0);
            }
        };

        LocalDateTime end = now;

        Page<Object[]> result = transactionRepository.findTopSellerByPeriod(
                start,
                end,
                Pageable.ofSize(1)
        );

        if (result.isEmpty()) {
            throw new ServiceException(
                    ErrorCode.NOT_FOUND,
                    "No transactions found for this period"
            );
        }

        Object[] row = result.getContent().get(0);

        Long sellerId = (Long) row[0];
        BigDecimal total = (BigDecimal) row[1];

        Seller seller = sellerRepository.findById(sellerId)
                .orElseThrow(() -> new ServiceException(
                        ErrorCode.SELLER_NOT_FOUND,
                        "Seller not found"
                ));

        return MostProductiveResponse.builder()
                .sellerId(sellerId)
                .name(seller.getName())
                .totalAmount(total)
                .build();
    }

    @Override
    public PageResponse<SellerTotalResponse> getSellersWithTotalLessThan(
            PeriodType period,
            BigDecimal amount,
            Pageable pageable
    ) {

        LocalDateTime now = LocalDateTime.now();

        LocalDateTime start = switch (period) {
            case DAY -> now.toLocalDate().atStartOfDay();
            case MONTH -> now.withDayOfMonth(1).toLocalDate().atStartOfDay();
            case YEAR -> now.withDayOfYear(1).toLocalDate().atStartOfDay();
            case QUARTER -> {
                int currentMonth = now.getMonthValue();
                int startMonth = ((currentMonth - 1) / 3) * 3 + 1;
                yield LocalDateTime.of(now.getYear(), startMonth, 1, 0, 0);
            }
        };

        LocalDateTime end = now;

        Page<Object[]> page = transactionRepository.findSellersWithTotalLessThan(
                start,
                end,
                amount,
                pageable
        );

        return PageResponse.<SellerTotalResponse>builder()
                .items(page.getContent().stream()
                        .map(row -> {
                            Long sellerId = (Long) row[0];
                            BigDecimal total = (BigDecimal) row[1];

                            Seller seller = sellerRepository.findById(sellerId)
                                    .orElseThrow(() -> new ServiceException(
                                            ErrorCode.SELLER_NOT_FOUND,
                                            "Seller not found"
                                    ));

                            return SellerTotalResponse.builder()
                                    .sellerId(sellerId)
                                    .name(seller.getName())
                                    .totalAmount(total)
                                    .build();
                        })
                        .toList())
                .page(page.getNumber())
                .size(page.getSize())
                .total(page.getTotalElements())
                .hasNext(page.hasNext())
                .build();
    }

    @Override
    public BestPeriodResponse getBestPeriod(
            Long sellerId,
            PeriodType period,
            LocalDateTime from,
            LocalDateTime to
    ) {

        // 1. Проверка продавца
        sellerRepository.findById(sellerId)
                .orElseThrow(() -> new ServiceException(
                        ErrorCode.SELLER_NOT_FOUND,
                        "Seller with id " + sellerId + " not found"
                ));

        // 2. Проверка диапазона
        if (from.isAfter(to)) {
            throw new ServiceException(
                    ErrorCode.BAD_REQUEST,
                    "from must be before to"
            );
        }

        // 3. Запрос
        Page<Object[]> result = switch (period) {
            case DAY -> transactionRepository.findBestDay(sellerId, from, to, Pageable.ofSize(1));
            case MONTH -> transactionRepository.findBestMonth(sellerId, from, to, Pageable.ofSize(1));
            case QUARTER -> transactionRepository.findBestQuarter(sellerId, from, to, Pageable.ofSize(1));
            case YEAR -> transactionRepository.findBestYear(sellerId, from, to, Pageable.ofSize(1));
        };

        if (result.isEmpty()) {
            throw new ServiceException(
                    ErrorCode.NOT_FOUND,
                    "No transactions in given range"
            );
        }

        Object[] row = result.getContent().get(0);

        // 4. Безопасное преобразование даты
        Object dateObj = row[0];
        LocalDateTime start;

        if (dateObj instanceof java.sql.Timestamp ts) {
            start = ts.toLocalDateTime();
        } else if (dateObj instanceof java.sql.Date d) {
            start = d.toLocalDate().atStartOfDay();
        } else if (dateObj instanceof LocalDateTime ldt) {
            start = ldt;
        } else {
            throw new RuntimeException("Unknown date type: " + dateObj.getClass());
        }

        BigDecimal total = (BigDecimal) row[1];

        // 5. Конец периода
        LocalDateTime end = switch (period) {
            case DAY -> start.plusDays(1).minusSeconds(1);
            case MONTH -> start.plusMonths(1).minusSeconds(1);
            case QUARTER -> start.plusMonths(3).minusSeconds(1);
            case YEAR -> start.plusYears(1).minusSeconds(1);
        };

        return BestPeriodResponse.builder()
                .startDate(start)
                .endDate(end)
                .totalAmount(total)
                .build();
    }
    
}