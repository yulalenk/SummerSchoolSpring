package com.example.spring.service;


import com.example.spring.entity.Question;
import com.example.spring.repository.QuestionRepository;
import com.example.spring.repository.UserRepository;
import com.example.spring.entity.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService implements UserDetailsService {

    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return find(username);
    }

    private final UserRepository userRepository;
    private final QuestionRepository questionRepository;

    public UserService(UserRepository userRepository, QuestionRepository questionRepository) {

        this.userRepository = userRepository;
        this.questionRepository= questionRepository;
    }

    public User save(User user) {
        return userRepository.saveAndFlush(user);
    }

    public User find(String userName) {

        return userRepository.findOneByUsername(userName);
    }

    public void invalidateToken(String username) {
        User user = userRepository.findOneByUsername(username);
        user.setAuthToken(null);
        userRepository.save(user);
    }

    public User setResult(String username, Integer result) {
        User user = userRepository.findOneByUsername(username);
        System.out.println(user.getUsername()+" " + result);
        user.setResult(result);
        userRepository.save(user);

        return user;
    }

    public User getCurrentUser(String username) {
        User user = userRepository.findOneByUsername(username);
        return user;
    }


    public User findByAuthToken(String token) {

        return userRepository.findByAuthTokenEquals(token);
    }

    public List<User> getAll(){
        return
                userRepository.findAllByRole("user");
    }

    public List<Question> getAllQuestions(){
        return questionRepository.findAll();
    }

}
