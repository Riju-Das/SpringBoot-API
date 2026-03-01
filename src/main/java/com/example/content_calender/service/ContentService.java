package com.example.content_calender.service;

import com.example.content_calender.dto.ContentResponseDto;
import com.example.content_calender.model.Content;
import com.example.content_calender.model.Status;
import com.example.content_calender.model.User;
import com.example.content_calender.repository.ContentCollectionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ContentService {

    private final ContentCollectionRepository contentCollectionRepository;




    public List<ContentResponseDto> findContentByUser(User user) {
        return  contentCollectionRepository.findByUser(user).stream()
                .map(ContentResponseDto::from)
                .toList();
    }

    public ContentResponseDto findContentById(User user, Integer id) {
        Content contentres = contentCollectionRepository.findById(id)
                .filter(content -> content.getUser().equals(user))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Content not found"));
        return ContentResponseDto.from(contentres);
    }

    public ContentResponseDto createContent(ContentResponseDto contentdto, User user) {
        Content content = new Content();
        content.setTitle(contentdto.getTitle());
        content.setDesc(contentdto.getDescription());
        content.setStatus(contentdto.getStatus());
        content.setContentType(contentdto.getContentType());
        content.setDateCreated(LocalDateTime.now());
        content.setDateUpdated(LocalDateTime.now());
        content.setUser(user);
        return ContentResponseDto.from(contentCollectionRepository.save(content));
    }

    public ContentResponseDto updateContent(ContentResponseDto content, Integer id, User user) {
        Content existingContent = contentCollectionRepository.findById(id)
                .filter(c -> c.getUser().equals(user))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Content not found"));
        existingContent.setTitle(content.getTitle());
        existingContent.setDesc(content.getDescription());
        existingContent.setDateUpdated(LocalDateTime.now());
        existingContent.setStatus(content.getStatus());
        existingContent.setContentType(content.getContentType());

        return ContentResponseDto.from(contentCollectionRepository.save(existingContent));
    }

    public void deleteContent(Integer id, User user) {
        Content existingContent = contentCollectionRepository.findById(id)
                .filter(c -> c.getUser().equals(user))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Content not found"));
        contentCollectionRepository.delete(existingContent);
    }
    public List<ContentResponseDto> findTitleByKeyword(String keyword, User user) {
        return contentCollectionRepository.findByTitleContainingIgnoreCaseAndUser(keyword,user).stream()
                .map(ContentResponseDto::from)
                .toList();
    }

    public List<ContentResponseDto> getContentByStatus(Status status, User user) {
        return contentCollectionRepository.findByStatusAndUser(status,user).stream()
                .map(ContentResponseDto::from)
                .toList();
    }
}
