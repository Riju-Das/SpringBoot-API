package com.example.content_calender.controller;

import com.example.content_calender.dto.LoginRequestDto;
import com.example.content_calender.dto.LoginResponseDto;
import com.example.content_calender.dto.SignupResponseDto;
import com.example.content_calender.dto.TokenRefreshResponseDto;
import com.example.content_calender.security.AuthService;
import com.example.content_calender.security.RefreshTokenService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final RefreshTokenService refreshTokenService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@Valid @RequestBody LoginRequestDto loginRequestDto) {
        LoginResponseDto responseDto = authService.login(loginRequestDto);
        ResponseCookie springCookie = ResponseCookie.from("refreshToken", responseDto.getRefreshToken())
                .httpOnly(true)
                .secure(false)
                .path("/api/auth/refresh")
                .maxAge(7 * 24 * 60 * 60)
                .sameSite("Strict")
                .build();
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, springCookie.toString())
                .body(responseDto);
    }

    @PostMapping("/refresh")
    public ResponseEntity<TokenRefreshResponseDto> refresh(@CookieValue(name="refreshToken", required = false) String refreshToken){

        if(refreshToken==null || refreshToken.isBlank()){
            throw  new ResponseStatusException(HttpStatus.UNAUTHORIZED,"Please login again");
        }
        String newAccessToken = authService.refresh(refreshToken);
        return ResponseEntity.ok(new TokenRefreshResponseDto(newAccessToken));
    }

    @PostMapping("/signup")
    public ResponseEntity<SignupResponseDto> signup(@Valid @RequestBody LoginRequestDto signupRequestDto){
        return ResponseEntity.ok(authService.signup(signupRequestDto));
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(@CookieValue(name="refreshToken", required = false) String refreshToken){
        if(refreshToken!=null && !refreshToken.isBlank()){
            refreshTokenService.deleteByToken(refreshToken);
        }
        ResponseCookie cleanCookie = ResponseCookie.from("refreshToken", "")
                .httpOnly(true)
                .path("/api/auth/refresh")
                .maxAge(0)
                .build();
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cleanCookie.toString())
                .body("Logged out successfully");
    }
}
