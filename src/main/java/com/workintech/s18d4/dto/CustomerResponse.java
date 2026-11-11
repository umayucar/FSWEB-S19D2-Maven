package com.workintech.s18d4.dto;

public record CustomerResponse(
        Long id,
        String email,
        Double salary
) {
}