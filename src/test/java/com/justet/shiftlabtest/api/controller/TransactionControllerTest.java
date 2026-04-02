package com.justet.shiftlabtest.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.justet.shiftlabtest.api.ApiPath;
import com.justet.shiftlabtest.api.model.PageResponse;
import com.justet.shiftlabtest.api.model.transaction.TransactionRequest;
import com.justet.shiftlabtest.api.model.transaction.TransactionResponse;
import com.justet.shiftlabtest.core.entity.PaymentType;
import com.justet.shiftlabtest.core.service.TransactionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class TransactionControllerTest {

    private MockMvc mockMvc;

    @Mock
    private TransactionService transactionService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        TransactionController controller =
                new TransactionController(transactionService);

        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .build();

        objectMapper.findAndRegisterModules();
    }

    private TransactionResponse response(Long id) {
        return TransactionResponse.builder()
                .id(id)
                .sellerId(1L)
                .amount(BigDecimal.valueOf(100))
                .paymentType(PaymentType.CARD)
                .transactionDate(LocalDateTime.now())
                .build();
    }

    @Test
    void create_shouldReturnTransaction() throws Exception {

        TransactionRequest request = TransactionRequest.builder()
                .sellerId(1L)
                .amount(BigDecimal.valueOf(100))
                .paymentType(PaymentType.CARD)
                .build();

        when(transactionService.create(any()))
                .thenReturn(response(1L));

        mockMvc.perform(post(ApiPath.TRANSACTIONS)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));

        verify(transactionService).create(any());
    }

    @Test
    void getAll_shouldReturnPageResponse() throws Exception {

        PageResponse<TransactionResponse> page = PageResponse.<TransactionResponse>builder()
                .items(List.of(response(1L), response(2L)))
                .page(0)
                .size(2)
                .total(2)
                .hasNext(false)
                .build();

        when(transactionService.getAll(any())).thenReturn(page);

        mockMvc.perform(get(ApiPath.TRANSACTIONS))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items[0].id").value(1))
                .andExpect(jsonPath("$.items[1].id").value(2));

        verify(transactionService).getAll(any());
    }

    @Test
    void getById_shouldReturnTransaction() throws Exception {

        when(transactionService.getById(1L))
                .thenReturn(response(1L));

        mockMvc.perform(get(ApiPath.TRANSACTIONS + "/{transactionId}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));

        verify(transactionService).getById(1L);
    }

    @Test
    void getBySeller_shouldReturnTransactions() throws Exception {

        PageResponse<TransactionResponse> page = PageResponse.<TransactionResponse>builder()
                .items(List.of(response(1L), response(2L)))
                .page(0)
                .size(2)
                .total(2)
                .hasNext(false)
                .build();

        when(transactionService.getBySeller(eq(1L), any()))
                .thenReturn(page);

        mockMvc.perform(get(ApiPath.TRANSACTIONS + "/seller/{sellerId}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items[0].id").value(1))
                .andExpect(jsonPath("$.items[1].id").value(2));

        verify(transactionService).getBySeller(eq(1L), any());
    }
}