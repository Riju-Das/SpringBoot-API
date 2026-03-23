package com.example.content_calender.repository;

import com.example.content_calender.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.content_calender.model.Content;
import com.example.content_calender.model.Type;
import com.example.content_calender.model.Status;

import java.util.List;


@Repository
public  interface ContentCollectionRepository extends JpaRepository<Content,Integer>{

    Page<Content> findByStatusAndUser(Status status, User user, Pageable pageable);
    Page<Content> findByTitleContainingIgnoreCaseAndUser(String keyword,User user, Pageable pageable);
    Page<Content> findByUser(User user, Pageable pageable);

}