package com.justet.shiftlabtest.core.repository;

import com.justet.shiftlabtest.core.entity.Seller;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
@SpringBootTest
class SellerRepositoryTest {

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
    private SellerRepository sellerRepository;

    @BeforeEach
    void setUp() {
        sellerRepository.deleteAll();
    }

    @Test
    void shouldSaveAndFindSeller() {

        Seller seller = Seller.builder()
                .name("John Doe")
                .contactInfo("john@example.com")
                .build();

        Seller saved = sellerRepository.save(seller);
        Seller found = sellerRepository.findById(saved.getId()).orElse(null);

        assertThat(found).isNotNull();
        assertThat(found.getName()).isEqualTo("John Doe");
        assertThat(found.getContactInfo()).isEqualTo("john@example.com");
        assertThat(found.getRegistrationDate()).isNotNull();
    }

    @Test
    void shouldFindAllWithPagination() {

        Seller s1 = Seller.builder().name("S1").contactInfo("s1@mail.com").build();
        Seller s2 = Seller.builder().name("S2").contactInfo("s2@mail.com").build();
        Seller s3 = Seller.builder().name("S3").contactInfo("s3@mail.com").build();

        sellerRepository.saveAll(List.of(s1, s2, s3));

        Page<Seller> result = sellerRepository.findAll(PageRequest.of(0, 2));

        assertThat(result.getContent()).hasSize(2);
        assertThat(result.getTotalElements()).isEqualTo(3);
        assertThat(result.getTotalPages()).isEqualTo(2);
    }

    @Test
    void shouldUpdateSeller() {

        Seller seller = Seller.builder()
                .name("Old Name")
                .contactInfo("old@mail.com")
                .build();

        Seller saved = sellerRepository.save(seller);

        saved.setName("New Name");
        saved.setContactInfo("new@mail.com");

        Seller updated = sellerRepository.save(saved);

        assertThat(updated.getName()).isEqualTo("New Name");
        assertThat(updated.getContactInfo()).isEqualTo("new@mail.com");
    }

    @Test
    void shouldDeleteSeller() {

        Seller seller = Seller.builder()
                .name("To Delete")
                .contactInfo("delete@mail.com")
                .build();

        Seller saved = sellerRepository.save(seller);

        sellerRepository.deleteById(saved.getId());

        assertThat(sellerRepository.findById(saved.getId())).isEmpty();
    }
}