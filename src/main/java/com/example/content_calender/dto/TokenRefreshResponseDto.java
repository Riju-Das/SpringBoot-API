package com.example.content_calender.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TokenRefreshResponseDto {
    private String jwt;
}
