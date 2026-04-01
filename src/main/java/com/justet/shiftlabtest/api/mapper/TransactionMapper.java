package com.justet.shiftlabtest.api.mapper;

import com.justet.shiftlabtest.api.dto.transaction.TransactionResponse;
import com.justet.shiftlabtest.core.entity.Transaction;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TransactionMapper {

    @Mapping(source = "seller.id", target = "sellerId")
    TransactionResponse toResponse(Transaction transaction);
}