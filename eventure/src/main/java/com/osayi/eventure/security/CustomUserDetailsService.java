package com.osayi.eventure.security;

import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import com.osayi.eventure.model.User;
import com.osayi.eventure.repository.UserRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepo;

    public CustomUserDetailsService(UserRepository userRepo) {
        this.userRepo = userRepo;
    }

    // Qui riceviamo "username" dal form di login, ma noi lo trattiamo come email
    @Override
    public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {
        User user = userRepo.findByEmail(usernameOrEmail)
            .orElseThrow(() -> new UsernameNotFoundException("Utente non trovato: " + usernameOrEmail));

        // mappiamo ruolo -> granted authority (Spring si aspetta "ROLE_..." internamente)
        GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + user.getRuolo().name());
        return org.springframework.security.core.userdetails.User.builder()
            .username(user.getEmail())        // user identifier shown by Spring (usiamo email)
            .password(user.getPassword())     // password già codificata con BCrypt
            .authorities(List.of(authority))
            .build();
    }
}
