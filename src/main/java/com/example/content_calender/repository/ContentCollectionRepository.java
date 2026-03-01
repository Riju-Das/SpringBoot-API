package com.example.content_calender.repository;

import com.example.content_calender.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.content_calender.model.Content;
import com.example.content_calender.model.Type;
import com.example.content_calender.model.Status;

import java.util.List;


@Repository
public  interface ContentCollectionRepository extends JpaRepository<Content,Integer>{

    List<Content> findByStatusAndUser(Status status, User user);
    List<Content> findByTitleContainingIgnoreCaseAndUser(String keyword,User user);
    List<Content> findByUser(User user);

}