package com.workintech.s18d4.dto;

public record AccountResponse(
        Long id,
        String accountName,
        Double moneyAmount,
        Long customerId
) {
}