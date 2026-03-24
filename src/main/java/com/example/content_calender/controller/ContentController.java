package com.example.content_calender.controller;

import com.example.content_calender.dto.ContentResponseDto;
import com.example.content_calender.model.User;
import com.example.content_calender.repository.UserRepository;
import com.example.content_calender.service.ContentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.*;
import com.example.content_calender.model.Content;
import com.example.content_calender.repository.ContentCollectionRepository;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.content_calender.model.Status;





@RestController
@RequestMapping("/api/content")
@RequiredArgsConstructor
@CrossOrigin
@Validated
public class ContentController {

    private final ContentCollectionRepository repository;
    private final UserRepository userRepository;
    private final ContentService contentService;

    private Pageable buildPageable(int page, int size , String sortBy , String direction){
        Sort sort = direction.equalsIgnoreCase("asc")
                ?Sort.by(sortBy).ascending()
                :Sort.by(sortBy).descending();
        return PageRequest.of(page,size,sort);
    }

    @GetMapping("")
    public ResponseEntity<Page<ContentResponseDto>> findAll(
            Authentication authentication,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "dateCreated") String sortBy,
            @RequestParam(defaultValue = "desc") String direction
    ){
        String username = authentication.getName();
        User user = userRepository.findByUsername(username).orElseThrow();
        Pageable pageable = buildPageable(page,size,sortBy,direction);
        return ResponseEntity.ok(contentService.findContentByUser(user,pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ContentResponseDto> findContentById(@PathVariable Integer id,Authentication authentication) {
        String username = authentication.getName();
        User user = userRepository.findByUsername(username).orElseThrow();
        return ResponseEntity.ok(contentService.findContentById(user,id));
    }

    @PostMapping("")
    public ResponseEntity<ContentResponseDto> create(@Valid @RequestBody ContentResponseDto content, Authentication authentication){
        String username = authentication.getName();
        User user = userRepository.findByUsername(username).orElseThrow();
        return ResponseEntity.status(HttpStatus.CREATED).body(contentService.createContent(content ,user));

    }

    @PutMapping("/{id}")
    public ResponseEntity<ContentResponseDto> update(@RequestBody ContentResponseDto content, @PathVariable Integer id, Authentication authentication){
        String username = authentication.getName();
        User user = userRepository.findByUsername(username).orElseThrow();
        return ResponseEntity.ok(contentService.updateContent(content,id,user));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id, Authentication authentication){
        String username = authentication.getName();
        User user = userRepository.findByUsername(username).orElseThrow();
        contentService.deleteContent(id,user);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }


    @GetMapping("/filter/keyword/{keyword}")
    public ResponseEntity<Page<ContentResponseDto>> findTitleByKeyword(
            @PathVariable String keyword,
            Authentication authentication,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "dateCreated") String sortBy,
            @RequestParam(defaultValue = "desc") String direction
            ) {
        String username = authentication.getName();
        User user = userRepository.findByUsername(username).orElseThrow();
        Pageable pageable = buildPageable(page,size,sortBy,direction);
        return ResponseEntity.ok(contentService.findTitleByKeyword(keyword,user, pageable));
    }
    
    @GetMapping("/filter/status/{status}")
    public ResponseEntity<Page<ContentResponseDto>> getContentByStatus(
            @PathVariable Status status,
            Authentication authentication,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "dateCreated") String sortBy,
            @RequestParam(defaultValue = "desc") String direction

    ) {
        String username = authentication.getName();
        User user = userRepository.findByUsername(username).orElseThrow();
        Pageable pageable = buildPageable(page,size,sortBy,direction);
        return ResponseEntity.ok(contentService.getContentByStatus(status, user,pageable));
    }
    
}
