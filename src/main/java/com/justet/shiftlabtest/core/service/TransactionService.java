package com.justet.shiftlabtest.core.service;

import com.justet.shiftlabtest.api.dto.PageResponse;
import com.justet.shiftlabtest.api.dto.TransactionRequest;
import com.justet.shiftlabtest.api.dto.TransactionResponse;
import org.springframework.data.domain.Pageable;

public interface TransactionService {

    TransactionResponse create(TransactionRequest request);

    PageResponse<TransactionResponse> getAll(Pageable pageable);

    TransactionResponse getById(Long transactionId);

    PageResponse<TransactionResponse> getBySeller(Long sellerId, Pageable pageable);
}