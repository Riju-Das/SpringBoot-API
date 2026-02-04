package com.example.content_calender.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.content_calender.model.Content;
import com.example.content_calender.model.Type;
import com.example.content_calender.model.Status;

import java.util.List;


@Repository
public  interface ContentCollectionRepository extends JpaRepository<Content,Integer>{

    List<Content> findByStatus(Status status);
    List<Content> findByContentType(Type contentType);
    List<Content> findByTitleContainingIgnoreCase(String keyword);


}