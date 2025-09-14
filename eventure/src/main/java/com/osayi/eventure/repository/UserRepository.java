package com.osayi.eventure.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.osayi.eventure.model.User;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmailIgnoreCase(String email);
    Optional<User> findByUsernameIgnoreCase(String username);

    Optional<User> findByEmailIgnoreCaseOrUsernameIgnoreCase(String email, String username);

    boolean existsByEmailIgnoreCase(String email);
    boolean existsByUsernameIgnoreCase(String username);

    // NEW: utile quando Facebook non fornisce l'email
    Optional<User> findByProviderAndProviderId(String provider, String providerId);
}
