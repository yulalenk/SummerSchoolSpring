package com.example.spring.repository;

import com.example.spring.entity.Quiz;
import com.example.spring.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuizRepository extends JpaRepository<Quiz, Long> {
//    Quiz findByUser(User user);
}
