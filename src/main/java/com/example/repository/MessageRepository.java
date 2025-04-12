package com.example.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.entity.Message;
import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Integer>{
    //search the Message table for all rows where the postedBy field equals the given value
    List<Message> findByPostedBy(Integer postedBy);
}
