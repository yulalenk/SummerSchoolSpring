package com.example.spring.dto;

import com.example.spring.entity.Question;
import com.example.spring.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpHeaders;

import java.security.Principal;
import java.util.List;

@Data
@AllArgsConstructor
public class ResponseMessage {
    private String message;
    private User user;
    private List<User> participants;
    private List<Question> questions;

    public ResponseMessage(String message){
        this.message = message;
    }

    public ResponseMessage(String message, User user){
        this.message = message;
        this.user = user;
    }

    public ResponseMessage(String message, User user, List<Question> questions){
        this.message = message;
        this.user = user;
        this.questions = questions;
    }
    public ResponseMessage(String message, List<User> participants){
        this.message = message;
        this.participants = participants;

    }

}
