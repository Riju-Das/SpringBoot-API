package com.example.content_calender.dto;

import com.example.content_calender.model.Content;
import com.example.content_calender.model.Status;
import com.example.content_calender.model.Type;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ContentResponseDto {
    private Integer id;
    private Long userId;
    private String title;
    private String description;
    private Status status;
    private Type contentType;
    private LocalDateTime dateCreated;
    private LocalDateTime dateUpdated;

    public static ContentResponseDto from(Content content){
        return new ContentResponseDto(
                content.getId(),
                content.getUser().getId(),
                content.getTitle(),
                content.getDesc(),
                content.getStatus(),
                content.getContentType(),
                content.getDateCreated(),
                content.getDateUpdated()
        );
    }
}
