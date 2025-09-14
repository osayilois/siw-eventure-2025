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

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        final String in = (login == null) ? "" : login.trim();

        // cerca per email O username, entrambe case-insensitive
        User user = userRepo.findByEmailIgnoreCaseOrUsernameIgnoreCase(in, in)
            .orElseThrow(() -> new UsernameNotFoundException("Utente non trovato: " + in));

        // mappa ruolo → authority (Spring vuole "ROLE_...")
        GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + user.getRuolo().name());

        // IMPORTANTISSIMO: esponi come principal name lo USERNAME
        // così il saluto mostra il nickname e non l'email
        return org.springframework.security.core.userdetails.User.builder()
            .username(user.getUsername())   // ← usiamo lo username come principal
            .password(user.getPassword())   // (già codificata BCrypt)
            .authorities(List.of(authority))
            .build();
    }
}
