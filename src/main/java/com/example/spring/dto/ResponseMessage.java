package com.example.spring.dto;

import com.example.spring.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ResponseMessage {
    private String message;
    private User user;

    public ResponseMessage(String message){
        this.message = message;
    }

}
