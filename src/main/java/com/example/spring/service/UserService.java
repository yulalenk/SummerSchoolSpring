package com.example.spring.service;


import com.example.spring.model.QuizData;
import com.example.spring.entity.Question;
import com.example.spring.entity.Quiz;
import com.example.spring.repository.QuestionRepository;
import com.example.spring.repository.QuizRepository;
import com.example.spring.repository.UserRepository;
import com.example.spring.entity.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService implements UserDetailsService {

    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return find(username);
    }

    private final UserRepository userRepository;
    private final QuestionRepository questionRepository;
    private final QuizRepository quizRepository;


    public UserService(UserRepository userRepository, QuestionRepository questionRepository, QuizRepository quizRepository) {

        this.userRepository = userRepository;
        this.questionRepository= questionRepository;
        this.quizRepository = quizRepository;    }

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

    public User setResult(String username, Integer result, QuizData quizData) {

        System.out.println("Пытаюсь");
        User user = userRepository.findOneByUsername(username);
        user.setQuiz(quizRepository.saveAndFlush(new Quiz(quizData.getAnswer1(),quizData.getAnswer2(),quizData.getAnswer3(),quizData.getAnswer4())));
//        newQuiz.setAnswer2(quizData.getAnswer2());
//        newQuiz.setAnswer1(quizData.getAnswer1());
//        newQuiz.setAnswer3(quizData.getAnswer3());
//        newQuiz.setAnswer4(quizData.getAnswer4());
//        quizRepository.saveAndFlush(newQuiz);
//        System.out.println(newQuiz);
        System.out.println(user);
        System.out.println(user.getUsername()+" " + result);
        user.setResult(result);
//        user.setQuiz(newQuiz);
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
