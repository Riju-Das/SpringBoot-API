package com.example.content_calender.model;


import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;
import org.springframework.data.redis.core.index.Indexed;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
@RedisHash("RefreshToken")
public class RefreshToken {
    @Id
    private String token;

    @Indexed
    private Long userId;

    @TimeToLive
    private Long expirationInSeconds;

}
