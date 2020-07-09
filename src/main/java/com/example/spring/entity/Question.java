package com.example.spring.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="QUESTIONS")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Question {
    @Id
    private String question;

    private String answer;
}