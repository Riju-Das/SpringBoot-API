package com.example.content_calender.controller;

import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.*;
import com.example.content_calender.model.Content;
import com.example.content_calender.repository.ContentCollectionRepository;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.content_calender.model.Status;





@RestController
@RequestMapping("/api/content")
@CrossOrigin
public class ContentController {

    private final ContentCollectionRepository repository;

    public ContentController(ContentCollectionRepository repository){
        this.repository = repository;
    }

    @GetMapping("")
    public List<Content> findAll(){
        return repository.findAll();
    }

    @GetMapping("/{id}")
    public Content getMethodName(@PathVariable Integer id) {
        return repository.findById(id).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND , "Content Not Found"));
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("")
    public Content create(@Valid @RequestBody Content content){
        content.setDateCreated(LocalDateTime.now());
        content.setDateUpdated(LocalDateTime.now());
        return repository.save(content);

    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping("/{id}")
    public Content update(@RequestBody Content content, @PathVariable Integer id){
        if(!repository.existsById(id)){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Content not found");
        }
        content.setId(id);
        content.setDateUpdated((LocalDateTime.now()));
        return repository.save(content);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id){
        if(!repository.existsById(id)){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Content not found");
        }        
        repository.deleteById(id);
    }


    @GetMapping("/filter/{keyword}")
    public List<Content> findTitleByKeyword(@PathVariable String keyword) {

        List<Content> results = repository.findByTitleContainingIgnoreCase(keyword);

        if(results.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Content Not Found");
        }
        
        return results;
    }
    
    @GetMapping("/filter/status/{status}")
    public List<Content> getMethodName(@PathVariable Status status) {
        List<Content> results = repository.findByStatus(status);
        if(results.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Content now found");
        }
        return results;
    }
    @GetMapping("/login")
    public String LoginPage(){
        return "hello";
    }
    
    
    
}
