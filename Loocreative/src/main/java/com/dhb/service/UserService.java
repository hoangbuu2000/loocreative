package com.dhb.service;

import com.dhb.pojo.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface UserService {
    Page<User> findAll(Pageable pageable);
    Optional<User> findByUsername(String username);
    Optional<User> findById(String id);
    User createOrUpdate(User user);
    void deleteAll();
    void deleteById(String id);
}
