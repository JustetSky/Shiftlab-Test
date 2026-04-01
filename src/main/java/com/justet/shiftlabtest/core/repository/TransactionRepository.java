package com.justet.shiftlabtest.core.repository;

import com.justet.shiftlabtest.core.entity.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    Page<Transaction> findBySellerId(Long sellerId, Pageable pageable);
    
    @Query("""
        SELECT t.seller.id, SUM(t.amount)
        FROM Transaction t
        WHERE t.transactionDate BETWEEN :start AND :end
        GROUP BY t.seller.id
        ORDER BY SUM(t.amount) DESC
    """)
    Page<Object[]> findTopSellerByPeriod(LocalDateTime start, LocalDateTime end, Pageable pageable);

    @Query("""
        SELECT t.seller.id, SUM(t.amount)
        FROM Transaction t
        WHERE t.transactionDate BETWEEN :start AND :end
        GROUP BY t.seller.id
        HAVING SUM(t.amount) < :amount
    """)
    Page<Object[]> findSellersWithTotalLessThan(LocalDateTime start, LocalDateTime end, BigDecimal amount, Pageable pageable);

    @Query("""
        SELECT DATE(t.transactionDate), SUM(t.amount)
        FROM Transaction t
        WHERE t.seller.id = :sellerId
          AND t.transactionDate BETWEEN :from AND :to
        GROUP BY DATE(t.transactionDate)
        ORDER BY SUM(t.amount) DESC
    """)
    Page<Object[]> findBestDay(Long sellerId, LocalDateTime from, LocalDateTime to,  Pageable pageable);

    @Query("""
        SELECT FUNCTION('DATE_TRUNC', 'month', t.transactionDate), SUM(t.amount)
        FROM Transaction t
        WHERE t.seller.id = :sellerId
          AND t.transactionDate BETWEEN :from AND :to
        GROUP BY FUNCTION('DATE_TRUNC', 'month', t.transactionDate)
        ORDER BY SUM(t.amount) DESC
    """)
    Page<Object[]> findBestMonth(Long sellerId, LocalDateTime from, LocalDateTime to, Pageable pageable);

    @Query("""
        SELECT FUNCTION('DATE_TRUNC', 'quarter', t.transactionDate), SUM(t.amount)
        FROM Transaction t
        WHERE t.seller.id = :sellerId
          AND t.transactionDate BETWEEN :from AND :to
        GROUP BY FUNCTION('DATE_TRUNC', 'quarter', t.transactionDate)
        ORDER BY SUM(t.amount) DESC
    """)
    Page<Object[]> findBestQuarter(Long sellerId, LocalDateTime from, LocalDateTime to,  Pageable pageable);

    @Query("""
        SELECT FUNCTION('DATE_TRUNC', 'year', t.transactionDate), SUM(t.amount)
        FROM Transaction t
        WHERE t.seller.id = :sellerId
          AND t.transactionDate BETWEEN :from AND :to
        GROUP BY FUNCTION('DATE_TRUNC', 'year', t.transactionDate)
        ORDER BY SUM(t.amount) DESC
    """)
    Page<Object[]> findBestYear(Long sellerId, LocalDateTime from, LocalDateTime to, Pageable pageable);
}