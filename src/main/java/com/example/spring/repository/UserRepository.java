package com.example.spring.repository;

import com.example.spring.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User findOneByUsername(String username);
    User findByAuthTokenEquals(String token);
}
