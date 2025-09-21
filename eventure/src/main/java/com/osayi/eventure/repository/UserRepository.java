/* package com.osayi.eventure.repository;

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
 */

 package com.osayi.eventure.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.osayi.eventure.model.User;
import com.osayi.eventure.model.User.AuthProvider;

public interface UserRepository extends JpaRepository<User, Long> {

    // --- Lookup case-insensitive su email/username ---
    Optional<User> findByEmailIgnoreCase(String email);
    Optional<User> findByUsernameIgnoreCase(String username);

    Optional<User> findByEmailIgnoreCaseOrUsernameIgnoreCase(String email, String username);

    boolean existsByEmailIgnoreCase(String email);
    boolean existsByUsernameIgnoreCase(String username);

    // Comodi quando aggiorni username/email e voglio escludere il record corrente
    boolean existsByUsernameIgnoreCaseAndIdNot(String username, Long id);
    boolean existsByEmailIgnoreCaseAndIdNot(String email, Long id);

    // --- Social login (quando Facebook può non dare email) ---
    Optional<User> findByProviderAndProviderId(AuthProvider provider, String providerId);
}
