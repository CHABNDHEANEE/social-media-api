package com.example.social.media.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
public class AuthResponseDto {
    private String accessToken;
    private final String tokenType = "Bearer ";

}
