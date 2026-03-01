package com.example.content_calender.model;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name="content")
@Getter
@Setter
@NoArgsConstructor
public class Content{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    @ManyToOne
    @JoinColumn(name="user_id", nullable=false)
    private User user;

    @NotBlank
    @Column(nullable = false)
    String title;

    @Column(name="description" , columnDefinition="TEXT")
    String desc;

    @Enumerated(EnumType.STRING)
    Status status;

    @Enumerated(EnumType.STRING)
    Type contentType;

    @Column(name="date_created")
    LocalDateTime dateCreated;

    @Column(name="date_updated")
    LocalDateTime dateUpdated;

}
