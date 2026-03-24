package com.example.content_calender.dto;

import com.example.content_calender.model.Content;
import com.example.content_calender.model.Status;
import com.example.content_calender.model.Type;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ContentResponseDto implements Serializable {

    private Integer id;
    private Long userId;

    @NotBlank(message = "title is required")
    private String title;

    @Size(max = 5000, message = "description must be <= 5000 characters")
    private String description;

    @NotNull(message = "status is required")
    private Status status;

    @NotNull(message = "contentType is required")
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
