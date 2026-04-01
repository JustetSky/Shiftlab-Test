package com.justet.shiftlabtest.core.mapper;

import com.justet.shiftlabtest.api.model.seller.SellerRequest;
import com.justet.shiftlabtest.api.model.seller.SellerResponse;
import com.justet.shiftlabtest.core.entity.Seller;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SellerMapper {

    SellerResponse toResponse(Seller seller);

    Seller toEntity(SellerRequest request);
}