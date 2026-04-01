package com.justet.shiftlabtest.core.service;

import com.justet.shiftlabtest.api.model.PageResponse;
import com.justet.shiftlabtest.api.model.seller.SellerRequest;
import com.justet.shiftlabtest.api.model.seller.SellerResponse;
import org.springframework.data.domain.Pageable;

public interface SellerService {

    SellerResponse createSeller(SellerRequest request);
    
    PageResponse<SellerResponse> getAllSellers(Pageable pageable);

    SellerResponse getSellerById(Long sellerId);

    SellerResponse updateSeller(Long sellerId, SellerRequest request);

    void deleteSeller(Long sellerId);
    
}