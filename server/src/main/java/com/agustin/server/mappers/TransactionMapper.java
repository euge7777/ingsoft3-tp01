package com.agustin.server.mappers;

import com.agustin.server.domain.entities.Transaction;
import com.agustin.server.dtos.responses.TransactionDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TransactionMapper {
    TransactionDTO toDTO(Transaction transaction);
    Transaction toEntity(TransactionDTO transactionDTO);
}
