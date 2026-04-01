package com.justet.shiftlabtest.core.mapper;

import com.justet.shiftlabtest.api.model.transaction.TransactionResponse;
import com.justet.shiftlabtest.core.entity.Transaction;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TransactionMapper {

    @Mapping(source = "seller.id", target = "sellerId")
    TransactionResponse toResponse(Transaction transaction);
}