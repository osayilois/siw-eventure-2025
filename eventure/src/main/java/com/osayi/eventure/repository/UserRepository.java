package com.osayi.eventure.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.osayi.eventure.model.User;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmailIgnoreCase(String email);
    Optional<User> findByUsernameIgnoreCase(String username);

    // 👇 questo è quello che useremo nel service
    Optional<User> findByEmailIgnoreCaseOrUsernameIgnoreCase(String email, String username);

    // utili per la registrazione
    boolean existsByEmailIgnoreCase(String email);
    boolean existsByUsernameIgnoreCase(String username);
}
