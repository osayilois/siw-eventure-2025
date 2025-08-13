package com.osayi.eventure.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.osayi.eventure.model.User;

public interface UserRepository extends JpaRepository <User, Long> {
    Optional<User> findByEmail(String email);
}
