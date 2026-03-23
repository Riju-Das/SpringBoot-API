package com.example.content_calender.dto;

import com.example.content_calender.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminUserResponseDto {
    private Long id;
    private String username;
    private Set<String> roles;

    public static AdminUserResponseDto from(User user){
        return new AdminUserResponseDto(
                user.getId(),
                user.getUsername(),
                user.getRoles().stream()
                        .map(Enum::name)
                        .collect(Collectors.toSet())
        );
    }
}
