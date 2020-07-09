package com.example.spring.repository;

import com.example.spring.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User findOneByUsername(String username);
    User findByAuthTokenEquals(String token);
    List<User> findAllByRole(String role);
}
