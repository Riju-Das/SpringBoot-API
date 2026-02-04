package com.example.content_calender.model;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Entity
@Table(name="content")
public class Content{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;


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


    public Content(){}

    public Content(Integer id, String title, String desc, Status status, Type contentType, LocalDateTime dateCreated, LocalDateTime dateUpdated ){
        this.id = id;
        this.title = title;
        this.desc = desc;
        this.status = status;
        this.contentType = contentType;
        this.dateCreated = dateCreated;
        this.dateUpdated = dateUpdated;
    }


    public Integer getId(){
        return id;
    }

    public void setId(Integer id){
        this.id = id;
    }

    public String getTitle(){
        return title;
    }

    public void setTitle(String title){
        this.title = title;
    }

    public String getDesc(){
        return desc;
    }

    public void setDesc(String desc){
        this.desc = desc;
    }

    public Status getStatus(){
        return status;
    }

    public void setStatus(Status status){
        this.status = status;
    }

    public Type getContentType(){
        return  contentType;
    }

    public void setContentType(Type contentType){
        this.contentType = contentType;
    }

    public LocalDateTime getDateCreated(){
        return dateCreated;
    } 
    
    public void setDateCreated(LocalDateTime dateCreated){
        this.dateCreated = dateCreated;
    }

    public LocalDateTime getDateUpdated(){
        return dateUpdated;
    }
    
    public void setDateUpdated(LocalDateTime dateUpdated){
        this.dateUpdated = dateUpdated;
    }
}
