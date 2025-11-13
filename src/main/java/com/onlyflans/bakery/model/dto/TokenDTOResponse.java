package com.onlyflans.bakery.model.dto;

public record TokenDTOResponse(
        String accessToken,
        String refreshToken
) {
}