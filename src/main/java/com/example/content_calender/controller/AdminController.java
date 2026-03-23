package com.example.content_calender.controller;

import com.example.content_calender.dto.AdminUserResponseDto;
import com.example.content_calender.dto.ContentResponseDto;
import com.example.content_calender.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    private Pageable buildPageable(int page, int size, String sortBy, String direction) {
        Sort sort = direction.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        return PageRequest.of(page, size, sort);
    }

    @GetMapping("/users")
    public ResponseEntity<Page<AdminUserResponseDto>> getAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "username") String sortBy,
            @RequestParam(defaultValue = "asc") String direction
    ) {
        Pageable pageable = buildPageable(page, size, sortBy, direction);
        return ResponseEntity.ok(adminService.getAllUsers(pageable));
    }

    @GetMapping("/content")
    public ResponseEntity<Page<ContentResponseDto>> getAllContent(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "dateCreated") String sortBy,
            @RequestParam(defaultValue = "asc") String direction
    ) {
        Pageable pageable = buildPageable(page, size, sortBy, direction);
        return ResponseEntity.ok(adminService.getAllContent(pageable));
    }

    @DeleteMapping("/content/{id}")
    public ResponseEntity<Void> deleteAnyContent(@PathVariable Integer id) {
        adminService.deleteAnyContent(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}