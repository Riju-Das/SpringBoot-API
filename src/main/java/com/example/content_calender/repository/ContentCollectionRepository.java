package com.example.content_calender.repository;

import org.springframework.stereotype.Repository;
import com.example.content_calender.model.Content;
import com.example.content_calender.model.Type;
import com.example.content_calender.model.Status;

import jakarta.annotation.PostConstruct;

import java.time.LocalDateTime;
import java.util.*;

@Repository
public class ContentCollectionRepository {
    private final List<Content> contentList = new ArrayList<>();

    public ContentCollectionRepository(){

    }

    public List<Content> findAll(){
        return contentList;
    }

    public Optional<Content> findById(Integer id){
        return contentList.stream().filter(c->c.id().equals(id)).findFirst();
    }

    public void save(Content content){
        contentList.removeIf(c->c.id().equals(content.id()));
        contentList.add(content);
    }

    public boolean ExistsById(Integer id){
        return contentList.stream().anyMatch(c->c.id().equals(id));
    }

    public void delete(Integer id){
        contentList.removeIf(c->c.id().equals(id));
    }

    @PostConstruct
    public void init(){
        Content c = new Content(
            1,
            "My first blog Post",
            "My first blog post",
            Status.IDEA,
            Type.ARTICLE,
            LocalDateTime.now(),
            null
        );

        contentList.add(c);
    }



}