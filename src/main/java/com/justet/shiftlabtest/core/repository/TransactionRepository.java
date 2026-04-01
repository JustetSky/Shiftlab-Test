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

    Page<Transaction> findByTransactionDateBetween(LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);

    Page<Transaction> findBySellerIdAndTransactionDateBetween(Long sellerId, LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);

    @Query("""
        SELECT t.seller.id, SUM(t.amount)
        FROM Transaction t
        WHERE t.transactionDate BETWEEN :start AND :end
        GROUP BY t.seller.id
        ORDER BY SUM(t.amount) DESC
    """)
    Page<Object[]> findTopSellerByPeriod( LocalDateTime start, LocalDateTime end, Pageable pageable
    );

    @Query("""
        SELECT t.seller.id, SUM(t.amount) as total
        FROM Transaction t
        WHERE t.transactionDate BETWEEN :start AND :end
        GROUP BY t.seller.id
        HAVING SUM(t.amount) < :amount
    """)
    Page<Object[]> findSellersWithTotalLessThan(LocalDateTime start, LocalDateTime end, BigDecimal amount, Pageable pageable);
}