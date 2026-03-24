package com.example.content_calender.security;

import com.example.content_calender.model.RefreshToken;
import com.example.content_calender.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    @Value("${app.jwtRefreshExpirationMs:604800000}")
    private Long refreshTokenDurationMs;

    private final RefreshTokenRepository refreshTokenRepository;

    public RefreshToken createRefreshToken(Long userId){

        refreshTokenRepository.deleteByUserId(userId);
        Long expirationInSeconds = refreshTokenDurationMs / 1000;

        RefreshToken refreshToken = RefreshToken.builder()
                .token(UUID.randomUUID().toString())
                .userId(userId)
                .expirationInSeconds(expirationInSeconds)
                .build();
        return refreshTokenRepository.save(refreshToken);
    }

    public Optional<RefreshToken> findByToken(String token){
        return refreshTokenRepository.findById(token);
    }

    public void deleteByToken(String token){
        refreshTokenRepository.deleteById(token);
    }


}
