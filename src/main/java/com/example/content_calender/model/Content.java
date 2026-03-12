package com.example.content_calender.model;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name="content")
@Getter
@Setter
@NoArgsConstructor
public class Content implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    @ManyToOne
    @JoinColumn(name="user_id", nullable=false)
    @JsonIgnore
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
