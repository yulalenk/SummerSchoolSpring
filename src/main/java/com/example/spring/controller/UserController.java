package com.example.spring.controller;

import com.example.spring.model.*;
import com.example.spring.entity.User;
import com.example.spring.security.TokenProvider;
import com.example.spring.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    private final AuthenticationManager authenticationManager;
    private final TokenProvider tokenProvider;

    private final JavaMailSender emailSender;

    public UserController(UserService userService, AuthenticationManager authenticationManager, TokenProvider tokenProvider, BCryptPasswordEncoder bCryptPasswordEncoder, JavaMailSender emailSender) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.tokenProvider = tokenProvider;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.emailSender = emailSender;
    }


    @CrossOrigin
    @PostMapping(value = "/register")
    public ResponseEntity<ResponseMessage> createUser(@RequestBody User newUser) {


        boolean admin = newUser.getUsername().contains("MERA");

        if (userService.find(newUser.getUsername()) != null) {
            logger.error("username Already exist " + newUser.getUsername());
            return new ResponseEntity<>(new ResponseMessage("User already exists"), HttpStatus.CONFLICT);
        }
        newUser.setRole(admin ? "admin" : "user");
        newUser.setPassword(bCryptPasswordEncoder.encode(newUser.getPassword()));
        userService.save(newUser);


        return new ResponseEntity<>(new ResponseMessage("User successfully created, you can try to login"), HttpStatus.OK);
    }

    @CrossOrigin
    @PostMapping("/login")
    public ResponseEntity<ResponseMessage> user(@RequestBody UserCredentials data) {

        try {
            String username = data.getUsername();
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, data.getPassword()));
            String token = tokenProvider.resolveToken(username);
            User user = userService.getCurrentUser(username);
            List questions = userService.getAllQuestions();
            return new ResponseEntity<>(new ResponseMessage(token, user, questions), HttpStatus.OK);
        } catch (AuthenticationException e) {
            return new ResponseEntity<>(new ResponseMessage("Wrong user or password"), HttpStatus.UNAUTHORIZED);
        }
    }

    @CrossOrigin
    @PostMapping(value = "/logout")
    public ResponseEntity<ResponseMessage> logout(Principal user) {
        try {
            logger.info("user " + user.getName() + " logged out");
            userService.invalidateToken(user.getName());
            return new ResponseEntity<>(new ResponseMessage("logout successful"), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(new ResponseMessage(e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @CrossOrigin
    @PostMapping(value = "/result")
    public ResponseEntity<ResponseMessage> setResult(Principal user, @RequestBody UserCredentials result) {
        System.out.println("Я зашел");
        System.out.println(result);
        try {
            User user1 = userService.setResult(user.getName(), result.getResult(),result.getQuizData());
            return new ResponseEntity<>(new ResponseMessage("Result added", user1), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(new ResponseMessage(e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @CrossOrigin
    @GetMapping(value = "/list")
    public ResponseEntity<ResponseMessage> getCurrent(Principal user) {
        try {
            List users = userService.getAll();
            return new ResponseEntity<>(new ResponseMessage("All users were got", users), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(new ResponseMessage(e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @CrossOrigin
    @PostMapping(value = "/email")
    public ResponseEntity<ResponseMessage> sendEmail(@RequestBody Pop list) {
        String[] emails = list.getEmails();
        System.out.println(emails);

        SimpleMailMessage message = new SimpleMailMessage();
        try {
            for (Object user : emails) {

                message.setTo(user.toString());
                message.setSubject("SummerSchool");
                message.setText("Congratulations! You have successfully completed the test, so we invite you to an interview.");
                this.emailSender.send(message);
            }
            return new ResponseEntity<>(new ResponseMessage("All is OK"), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(new ResponseMessage(e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }


    @MessageMapping("/hello")
    @SendTo("/topic/greetings")
    public Greeting greeting(HelloMessage message) throws Exception {
        return new Greeting("Hello, " + message.getName() + "!");
    }


}
