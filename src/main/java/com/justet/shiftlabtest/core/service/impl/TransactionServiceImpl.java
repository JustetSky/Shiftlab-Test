package com.justet.shiftlabtest.core.service.impl;

import com.justet.shiftlabtest.api.model.PageResponse;
import com.justet.shiftlabtest.api.model.transaction.TransactionRequest;
import com.justet.shiftlabtest.api.model.transaction.TransactionResponse;
import com.justet.shiftlabtest.api.mapper.TransactionMapper;
import com.justet.shiftlabtest.core.entity.Seller;
import com.justet.shiftlabtest.core.entity.Transaction;
import com.justet.shiftlabtest.core.exception.ErrorCode;
import com.justet.shiftlabtest.core.exception.ServiceException;
import com.justet.shiftlabtest.core.repository.SellerRepository;
import com.justet.shiftlabtest.core.repository.TransactionRepository;
import com.justet.shiftlabtest.core.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final SellerRepository sellerRepository;
    private final TransactionMapper transactionMapper;

    @Override
    public TransactionResponse create(TransactionRequest request) {

        Seller seller = sellerRepository.findById(request.getSellerId())
                .orElseThrow(() -> new ServiceException(
                        ErrorCode.SELLER_NOT_FOUND,
                        "Seller with id " + request.getSellerId() + " not found"
                ));

        Transaction transaction = Transaction.builder()
                .seller(seller)
                .amount(request.getAmount())
                .paymentType(request.getPaymentType())
                .build();

        Transaction saved = transactionRepository.save(transaction);

        return transactionMapper.toResponse(saved);
    }

    @Override
    public PageResponse<TransactionResponse> getAll(Pageable pageable) {

        Page<Transaction> page = transactionRepository.findAll(pageable);

        return PageResponse.<TransactionResponse>builder()
                .items(page.getContent().stream()
                        .map(transactionMapper::toResponse)
                        .toList())
                .page(page.getNumber())
                .size(page.getSize())
                .total(page.getTotalElements())
                .hasNext(page.hasNext())
                .build();
    }

    @Override
    public TransactionResponse getById(Long transactionId) {

        Transaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new ServiceException(
                        ErrorCode.TRANSACTION_NOT_FOUND,
                        "Transaction with id " + transactionId + " not found"
                ));

        return transactionMapper.toResponse(transaction);
    }

    @Override
    public PageResponse<TransactionResponse> getBySeller(Long sellerId, Pageable pageable) {

        Page<Transaction> page = transactionRepository.findBySellerId(sellerId, pageable);

        return PageResponse.<TransactionResponse>builder()
                .items(page.getContent().stream()
                        .map(transactionMapper::toResponse)
                        .toList())
                .page(page.getNumber())
                .size(page.getSize())
                .total(page.getTotalElements())
                .hasNext(page.hasNext())
                .build();
    }
}