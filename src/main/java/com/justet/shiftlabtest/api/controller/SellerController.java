package com.justet.shiftlabtest.api.controller;

import com.justet.shiftlabtest.api.constant.ApiPath;
import com.justet.shiftlabtest.api.dto.PageResponse;
import com.justet.shiftlabtest.api.dto.seller.SellerRequest;
import com.justet.shiftlabtest.api.dto.seller.SellerResponse;
import com.justet.shiftlabtest.core.service.SellerService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequestMapping(ApiPath.SELLERS)
@RequiredArgsConstructor
public class SellerController {

    private final SellerService sellerService;

    @Operation(summary = "Получить список продавцов")
    @GetMapping
    public PageResponse<SellerResponse> getAll(Pageable pageable) {
        return sellerService.getAllSellers(pageable);
    }

    @Operation(summary = "Создать продавца")
    @PostMapping
    public SellerResponse create(@RequestBody @Valid SellerRequest request) {
        return sellerService.createSeller(request);
    }

    @Operation(summary = "Получить продавца по ID")
    @GetMapping(ApiPath.SELLER_ID)
    public SellerResponse getById(@PathVariable Long sellerId) {
        return sellerService.getSellerById(sellerId);
    }

    @Operation(summary = "Обновить продавца")
    @PutMapping(ApiPath.SELLER_ID)
    public SellerResponse update(
            @PathVariable Long sellerId,
            @RequestBody @Valid SellerRequest request
    ) {
        return sellerService.updateSeller(sellerId, request);
    }

    @Operation(summary = "Удалить продавца")
    @DeleteMapping(ApiPath.SELLER_ID)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long sellerId) {
        sellerService.deleteSeller(sellerId);
    }
}