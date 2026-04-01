package com.justet.shiftlabtest.api.mapper;

import com.justet.shiftlabtest.api.dto.seller.SellerRequest;
import com.justet.shiftlabtest.api.dto.seller.SellerResponse;
import com.justet.shiftlabtest.core.entity.Seller;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SellerMapper {

    SellerResponse toResponse(Seller seller);

    Seller toEntity(SellerRequest request);
}