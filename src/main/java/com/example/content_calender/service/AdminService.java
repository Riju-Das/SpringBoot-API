package com.example.content_calender.service;

import com.example.content_calender.dto.AdminUserResponseDto;
import com.example.content_calender.dto.ContentResponseDto;
import com.example.content_calender.repository.ContentCollectionRepository;
import com.example.content_calender.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final UserRepository userRepository;
    private final ContentCollectionRepository contentCollectionRepository;

    public Page<AdminUserResponseDto> getAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable).map(AdminUserResponseDto::from);
    }

    public Page<ContentResponseDto> getAllContent(Pageable pageable) {
        return contentCollectionRepository.findAll(pageable).map(ContentResponseDto::from);
    }

    public void deleteAnyContent(Integer id) {
        if (!contentCollectionRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Content not found");
        }
        contentCollectionRepository.deleteById(id);
    }
}