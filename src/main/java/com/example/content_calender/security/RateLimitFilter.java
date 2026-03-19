package com.example.content_calender.security;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class RateLimitFilter extends OncePerRequestFilter {

    private final Map<String , Bucket> buckets  = new ConcurrentHashMap<>();

    private final Bandwidth authLimit = Bandwidth.builder()
            .capacity(5)
            .refillGreedy(5, Duration.ofMinutes(1))
            .build();

    private final Bandwidth generalLimit = Bandwidth.builder()
            .capacity(100)
            .refillGreedy(100,Duration.ofMinutes(1))
            .build();

    private Bucket getBucket(String ip , Bandwidth limit){
        return buckets.computeIfAbsent(
                ip,
                k->Bucket.builder()
                        .addLimit(limit)
                        .build()
        );
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String path = request.getRequestURI();
        String ip = request.getRemoteAddr();

        Bucket bucket;
        if(path.startsWith("/api/auth")){
            String key =  ip + "-auth";
            bucket = getBucket(key,authLimit);
        }
        else if(path.startsWith("api/content")){
            String key = ip + "-general";
            bucket = getBucket(key,generalLimit);
        }
        else{
            filterChain.doFilter(request,response);
            return;
        }
        if(bucket.tryConsume(1)){
            filterChain.doFilter(request,response);
        }
        else{
            response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
            response.setContentType("application/json");
            response.getWriter().write("{\"status\":429,\"error\":\"Too Many Requests\",\"message\":\"Too many attempts. Please try again later.\"}");
        }
    }
}
