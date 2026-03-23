package com.example.content_calender.security;

import com.example.content_calender.dto.*;
import com.example.content_calender.model.RefreshToken;
import com.example.content_calender.model.Role;
import com.example.content_calender.model.User;
import com.example.content_calender.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final AuthUtil authUtil;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RefreshTokenService refreshTokenService;


    public LoginResponseDto login(LoginRequestDto loginRequestDto) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequestDto.getUsername(),loginRequestDto.getPassword())
        );
        User user = (User) authentication.getPrincipal();

        String token = authUtil.generateAccessToken(user);

        RefreshToken refreshToken = refreshTokenService.createRefreshToken(user.getId());

        return new LoginResponseDto(token , refreshToken.getToken() ,user.getId());
    }

    public SignupResponseDto signup(LoginRequestDto signupRequestDto) {

        if(signupRequestDto.getUsername()==null || signupRequestDto.getPassword()==null || signupRequestDto.getUsername().isBlank() || signupRequestDto.getPassword().isBlank()){
            throw new IllegalArgumentException("Username and password are required");
        }

        User user = userRepository.findByUsername(signupRequestDto.getUsername()).orElse(null);
        if(user!=null) throw new IllegalArgumentException("User already exists");

        user = userRepository.save(User.builder()
                .username(signupRequestDto.getUsername())
                .password(passwordEncoder.encode(signupRequestDto.getPassword()))
                .roles(Set.of(Role.USER))
                .build()
        );

        return new SignupResponseDto(user.getId(),user.getUsername());


    }

    public String refresh(String requestRefreshToken){
        return refreshTokenService.findByToken(requestRefreshToken)
                .map(refreshToken -> {
                    User user = userRepository.findById(refreshToken.getUserId())
                            .orElseThrow();
                    return authUtil.generateAccessToken(user);
                })
                        .orElseThrow(() -> new RuntimeException("Refresh token is invalid or expired. Please sign in again."));
    }
}
