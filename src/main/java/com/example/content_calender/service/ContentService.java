package com.example.content_calender.service;

import com.example.content_calender.dto.ContentResponseDto;
import com.example.content_calender.model.Content;
import com.example.content_calender.model.Status;
import com.example.content_calender.model.User;
import com.example.content_calender.repository.ContentCollectionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ContentService {

    private final ContentCollectionRepository contentCollectionRepository;



    @Cacheable(cacheNames = "contents", key = "#user.id + ':' + #pageable.pageNumber + ':' + #pageable.pageSize + ':' + #pageable.sort.toString()")
    public Page<ContentResponseDto> findContentByUser(User user, Pageable pageable) {
        return  contentCollectionRepository.findByUser(user, pageable)
                .map(ContentResponseDto::from);

    }

    @Cacheable(cacheNames = "content", key = "#user.id + ':' + #id")
    public ContentResponseDto findContentById(User user, Integer id) {
        Content contentres = contentCollectionRepository.findById(id)
                .filter(content -> content.getUser().equals(user))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Content not found"));
        return ContentResponseDto.from(contentres);
    }

    @CacheEvict(value = "contents", allEntries = true)
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

    @Caching(
            evict = @CacheEvict(value = "contents", allEntries = true),
            put = @CachePut(value = "content",key= "#user.id + ':' + #id")
    )
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

    @Caching(
            evict = {
                    @CacheEvict(value = "contents", allEntries = true),
                    @CacheEvict(value = "content", key = "#user.id + ':' + #id")
            }
    )
    public void deleteContent(Integer id, User user) {
        Content existingContent = contentCollectionRepository.findById(id)
                .filter(c -> c.getUser().equals(user))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Content not found"));
        contentCollectionRepository.delete(existingContent);
    }


    public Page<ContentResponseDto> findTitleByKeyword(String keyword, User user, Pageable pageable) {
        return contentCollectionRepository.findByTitleContainingIgnoreCaseAndUser(keyword,user, pageable)
                .map(ContentResponseDto::from);
    }

    public Page<ContentResponseDto> getContentByStatus(Status status, User user, Pageable pageable) {
        return contentCollectionRepository.findByStatusAndUser(status,user, pageable)
                .map(ContentResponseDto::from);
    }
}
