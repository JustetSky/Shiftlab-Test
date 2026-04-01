package com.justet.shiftlabtest.core.service;

import com.justet.shiftlabtest.api.dto.PageResponse;
import com.justet.shiftlabtest.api.dto.SellerRequest;
import com.justet.shiftlabtest.api.dto.SellerResponse;
import org.springframework.data.domain.Pageable;

public interface SellerService {

    SellerResponse createSeller(SellerRequest request);
    
    PageResponse<SellerResponse> getAllSellers(Pageable pageable);

    SellerResponse getSellerById(Long sellerId);

    SellerResponse updateSeller(Long sellerId, SellerRequest request);

    void deleteSeller(Long sellerId);
    
}