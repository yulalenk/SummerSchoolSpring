package com.example.spring.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserCredentials implements Serializable {
    private String username;
    private String password;
    private Integer result;
    private QuizData quizData;
}
