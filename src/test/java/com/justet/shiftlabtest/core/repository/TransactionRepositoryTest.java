package com.justet.shiftlabtest.core.repository;

import com.justet.shiftlabtest.core.entity.PaymentType;
import com.justet.shiftlabtest.core.entity.Seller;
import com.justet.shiftlabtest.core.entity.Transaction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
@SpringBootTest
class TransactionRepositoryTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:17.5")
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "create-drop");
    }

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private SellerRepository sellerRepository;

    private Seller seller1;
    private Seller seller2;

    @BeforeEach
    void setUp() {
        transactionRepository.deleteAll();
        sellerRepository.deleteAll();

        seller1 = sellerRepository.save(
                Seller.builder().name("Seller1").contactInfo("s1@mail.com").build()
        );

        seller2 = sellerRepository.save(
                Seller.builder().name("Seller2").contactInfo("s2@mail.com").build()
        );
    }

    private Transaction tx(Seller seller, String amount, LocalDateTime date) {
        return Transaction.builder()
                .seller(seller)
                .amount(new BigDecimal(amount))
                .paymentType(PaymentType.CARD)
                .transactionDate(date)
                .build();
    }

    @Test
    void shouldFindBySellerId() {

        transactionRepository.save(tx(seller1, "100", LocalDateTime.now()));

        var result = transactionRepository.findBySellerId(
                seller1.getId(),
                Pageable.unpaged()
        );

        assertThat(result.getContent()).hasSize(1);
    }

    @Test
    void shouldFindTopSellerByPeriod() {

        LocalDateTime now = LocalDateTime.now();

        transactionRepository.save(tx(seller1, "100", now));
        transactionRepository.save(tx(seller1, "200", now));
        transactionRepository.save(tx(seller2, "50", now));

        var result = transactionRepository.findTopSellerByPeriod(
                now.minusDays(1),
                now.plusDays(1),
                Pageable.ofSize(1)
        );

        Object[] row = result.getContent().get(0);

        assertThat((Long) row[0]).isEqualTo(seller1.getId());
        assertThat((BigDecimal) row[1]).isEqualByComparingTo("300");
    }

    @Test
    void shouldFindSellersWithTotalLessThan() {

        LocalDateTime now = LocalDateTime.now();

        transactionRepository.save(tx(seller1, "100", now));
        transactionRepository.save(tx(seller2, "50", now));

        var result = transactionRepository.findSellersWithTotalLessThan(
                now.minusDays(1),
                now.plusDays(1),
                new BigDecimal("80"),
                Pageable.unpaged()
        );

        assertThat(result.getContent()).hasSize(1);
        assertThat((Long) result.getContent().get(0)[0]).isEqualTo(seller2.getId());
    }

    @Test
    void shouldFindBestDay() {

        LocalDateTime d1 = LocalDateTime.of(2026, 3, 10, 10, 0);
        LocalDateTime d2 = LocalDateTime.of(2026, 3, 11, 10, 0);

        transactionRepository.save(tx(seller1, "100", d1));
        transactionRepository.save(tx(seller1, "200", d2));

        var result = transactionRepository.findBestDay(
                seller1.getId(),
                d1.minusDays(1),
                d2.plusDays(1),
                Pageable.ofSize(1)
        );

        assertThat((BigDecimal) result.getContent().get(0)[1])
                .isEqualByComparingTo("200");
    }

    @Test
    void shouldFindBestMonth() {

        LocalDateTime march = LocalDateTime.of(2026, 3, 10, 10, 0);
        LocalDateTime april = LocalDateTime.of(2026, 4, 10, 10, 0);

        transactionRepository.save(tx(seller1, "300", march));
        transactionRepository.save(tx(seller1, "100", april));

        var result = transactionRepository.findBestMonth(
                seller1.getId(),
                march.minusDays(1),
                april.plusDays(1),
                Pageable.ofSize(1)
        );

        assertThat((BigDecimal) result.getContent().get(0)[1])
                .isEqualByComparingTo("300");
    }

    @Test
    void shouldFindBestQuarter() {

        LocalDateTime q1 = LocalDateTime.of(2026, 2, 10, 10, 0);
        LocalDateTime q2 = LocalDateTime.of(2026, 5, 10, 10, 0);

        transactionRepository.save(tx(seller1, "400", q1));
        transactionRepository.save(tx(seller1, "100", q2));

        var result = transactionRepository.findBestQuarter(
                seller1.getId(),
                q1.minusDays(1),
                q2.plusDays(1),
                Pageable.ofSize(1)
        );

        assertThat((BigDecimal) result.getContent().get(0)[1])
                .isEqualByComparingTo("400");
    }

    @Test
    void shouldFindBestYear() {

        LocalDateTime y1 = LocalDateTime.of(2025, 3, 10, 10, 0);
        LocalDateTime y2 = LocalDateTime.of(2026, 3, 10, 10, 0);

        transactionRepository.save(tx(seller1, "100", y1));
        transactionRepository.save(tx(seller1, "500", y2));

        var result = transactionRepository.findBestYear(
                seller1.getId(),
                y1.minusDays(1),
                y2.plusDays(1),
                Pageable.ofSize(1)
        );

        assertThat((BigDecimal) result.getContent().get(0)[1])
                .isEqualByComparingTo("500");
    }
}