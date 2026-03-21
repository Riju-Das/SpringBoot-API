package com.example.content_calender.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class LoginResponseDto {
    private String jwt;

    @JsonIgnore
    private String refreshToken;

    private Long userId;
}
