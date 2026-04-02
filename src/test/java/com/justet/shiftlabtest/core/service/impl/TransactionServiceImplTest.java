package com.justet.shiftlabtest.core.service.impl;

import com.justet.shiftlabtest.api.model.PageResponse;
import com.justet.shiftlabtest.api.model.transaction.TransactionRequest;
import com.justet.shiftlabtest.api.model.transaction.TransactionResponse;
import com.justet.shiftlabtest.core.entity.PaymentType;
import com.justet.shiftlabtest.core.entity.Seller;
import com.justet.shiftlabtest.core.entity.Transaction;
import com.justet.shiftlabtest.core.exception.ServiceException;
import com.justet.shiftlabtest.core.mapper.TransactionMapper;
import com.justet.shiftlabtest.core.repository.SellerRepository;
import com.justet.shiftlabtest.core.repository.TransactionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransactionServiceImplTest {

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private SellerRepository sellerRepository;

    @Mock
    private TransactionMapper transactionMapper;

    @InjectMocks
    private TransactionServiceImpl service;

    private Seller seller(Long id) {
        return Seller.builder()
                .id(id)
                .name("Seller " + id)
                .contactInfo("s@mail.com")
                .build();
    }

    private Transaction transaction(Long id, Seller seller) {
        return Transaction.builder()
                .id(id)
                .seller(seller)
                .amount(new BigDecimal("100"))
                .paymentType(PaymentType.CARD)
                .build();
    }

    private TransactionRequest request(Long sellerId) {
        return TransactionRequest.builder()
                .sellerId(sellerId)
                .amount(new BigDecimal("100"))
                .paymentType(PaymentType.CARD)
                .build();
    }

    private TransactionResponse response(Long id) {
        return TransactionResponse.builder()
                .id(id)
                .amount(new BigDecimal("100"))
                .paymentType(PaymentType.CARD)
                .build();
    }

    @Test
    void create_shouldSaveTransaction() {

        Seller seller = seller(1L);
        TransactionRequest request = request(1L);
        Transaction saved = transaction(1L, seller);
        TransactionResponse response = response(1L);

        when(sellerRepository.findById(1L)).thenReturn(Optional.of(seller));
        when(transactionRepository.save(any())).thenReturn(saved);
        when(transactionMapper.toResponse(saved)).thenReturn(response);

        TransactionResponse result = service.create(request);

        assertThat(result.getId()).isEqualTo(1L);

        verify(sellerRepository).findById(1L);
        verify(transactionRepository).save(any());
        verify(transactionMapper).toResponse(saved);
    }

    @Test
    void create_whenSellerNotFound_shouldThrow() {

        when(sellerRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.create(request(1L)))
                .isInstanceOf(ServiceException.class)
                .hasMessageContaining("Seller with id 1 not found");
    }

    @Test
    void getAll_shouldReturnPage() {

        Pageable pageable = PageRequest.of(0, 2);

        Seller seller = seller(1L);
        Transaction t1 = transaction(1L, seller);
        Transaction t2 = transaction(2L, seller);

        Page<Transaction> page = new PageImpl<>(List.of(t1, t2), pageable, 2);

        when(transactionRepository.findAll(pageable)).thenReturn(page);
        when(transactionMapper.toResponse(any()))
                .thenAnswer(inv -> response(((Transaction) inv.getArgument(0)).getId()));

        PageResponse<TransactionResponse> result = service.getAll(pageable);

        assertThat(result.getItems()).hasSize(2);
        assertThat(result.getTotal()).isEqualTo(2);
    }

    @Test
    void getById_whenExists_shouldReturn() {

        Seller seller = seller(1L);
        Transaction tx = transaction(1L, seller);

        when(transactionRepository.findById(1L)).thenReturn(Optional.of(tx));
        when(transactionMapper.toResponse(tx)).thenReturn(response(1L));

        TransactionResponse result = service.getById(1L);

        assertThat(result.getId()).isEqualTo(1L);
    }

    @Test
    void getById_whenNotFound_shouldThrow() {

        when(transactionRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.getById(1L))
                .isInstanceOf(ServiceException.class)
                .hasMessageContaining("Transaction with id 1 not found");
    }

    @Test
    void getBySeller_shouldReturnPage() {

        Pageable pageable = PageRequest.of(0, 10);

        Seller seller = seller(1L);
        Transaction tx = transaction(1L, seller);

        Page<Transaction> page = new PageImpl<>(List.of(tx), pageable, 1);

        when(transactionRepository.findBySellerId(1L, pageable)).thenReturn(page);
        when(transactionMapper.toResponse(tx)).thenReturn(response(1L));

        PageResponse<TransactionResponse> result =
                service.getBySeller(1L, pageable);

        assertThat(result.getItems()).hasSize(1);
        assertThat(result.getTotal()).isEqualTo(1);
    }
}