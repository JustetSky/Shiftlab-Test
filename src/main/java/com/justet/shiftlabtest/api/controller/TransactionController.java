package com.justet.shiftlabtest.api.controller;

import com.justet.shiftlabtest.api.ApiPath;
import com.justet.shiftlabtest.api.model.PageResponse;
import com.justet.shiftlabtest.api.model.transaction.TransactionRequest;
import com.justet.shiftlabtest.api.model.transaction.TransactionResponse;
import com.justet.shiftlabtest.core.service.TransactionService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequestMapping(ApiPath.TRANSACTIONS)
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @Operation(summary = "Создать транзакцию")
    @PostMapping
    public TransactionResponse create(@RequestBody @Valid TransactionRequest request) {
        return transactionService.create(request);
    }

    @Operation(summary = "Получить все транзакции")
    @GetMapping
    public PageResponse<TransactionResponse> getAll(Pageable pageable) {
        return transactionService.getAll(pageable);
    }

    @Operation(summary = "Получить транзакцию по ID")
    @GetMapping(ApiPath.TRANSACTION_ID)
    public TransactionResponse getById(@PathVariable Long transactionId) {
        return transactionService.getById(transactionId);
    }

    @Operation(summary = "Получить транзакции продавца")
    @GetMapping(ApiPath.SELLER_TRANSACTIONS)
    public PageResponse<TransactionResponse> getBySeller(
            @PathVariable Long sellerId,
            Pageable pageable
    ) {
        return transactionService.getBySeller(sellerId, pageable);
    }
}