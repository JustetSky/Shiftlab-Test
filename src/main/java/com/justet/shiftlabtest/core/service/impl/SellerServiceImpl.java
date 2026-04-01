package com.justet.shiftlabtest.core.service.impl;

import com.justet.shiftlabtest.api.dto.PageResponse;
import com.justet.shiftlabtest.api.dto.seller.SellerRequest;
import com.justet.shiftlabtest.api.dto.seller.SellerResponse;
import com.justet.shiftlabtest.api.mapper.SellerMapper;
import com.justet.shiftlabtest.core.entity.Seller;
import com.justet.shiftlabtest.core.exception.ErrorCode;
import com.justet.shiftlabtest.core.exception.ServiceException;
import com.justet.shiftlabtest.core.repository.SellerRepository;
import com.justet.shiftlabtest.core.service.SellerService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SellerServiceImpl implements SellerService {

    private final SellerRepository sellerRepository;
    private final SellerMapper sellerMapper;

    @Override
    public SellerResponse createSeller(SellerRequest request) {

        Seller seller = sellerMapper.toEntity(request);

        Seller saved = sellerRepository.save(seller);

        return sellerMapper.toResponse(saved);
    }

    @Override
    public PageResponse<SellerResponse> getAllSellers(Pageable pageable) {

        Page<Seller> page = sellerRepository.findAll(pageable);

        return PageResponse.<SellerResponse>builder()
                .items(page.getContent().stream()
                        .map(sellerMapper::toResponse)
                        .toList())
                .page(page.getNumber())
                .size(page.getSize())
                .total(page.getTotalElements())
                .hasNext(page.hasNext())
                .build();
    }

    @Override
    public SellerResponse getSellerById(Long sellerId) {

        Seller seller = sellerRepository.findById(sellerId)
                .orElseThrow(() -> new ServiceException(
                        ErrorCode.SELLER_NOT_FOUND,
                        "Seller with id " + sellerId + " not found"
                ));

        return sellerMapper.toResponse(seller);
    }

    @Override
    public SellerResponse updateSeller(Long sellerId, SellerRequest request) {

        Seller seller = sellerRepository.findById(sellerId)
                .orElseThrow(() -> new ServiceException(
                        ErrorCode.SELLER_NOT_FOUND,
                        "Seller with id " + sellerId + " not found"
                ));

        seller.setName(request.getName());
        seller.setContactInfo(request.getContactInfo());

        Seller updated = sellerRepository.save(seller);

        return sellerMapper.toResponse(updated);
    }

    @Override
    public void deleteSeller(Long sellerId) {

        Seller seller = sellerRepository.findById(sellerId)
                .orElseThrow(() -> new ServiceException(
                        ErrorCode.SELLER_NOT_FOUND,
                        "Seller with id " + sellerId + " not found"
                ));

        sellerRepository.delete(seller);
    }
    
}