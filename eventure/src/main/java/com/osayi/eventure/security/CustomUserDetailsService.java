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

        User user = userRepo.findByEmailIgnoreCaseOrUsernameIgnoreCase(in, in)
            .orElseThrow(() -> new UsernameNotFoundException("Utente non trovato: " + in));

        GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + user.getRuolo().name());

        // se è un utente OAuth, la password è null: passiamo un placeholder innocuo
        String pwd = (user.getPassword() == null) ? "{noop}" : user.getPassword();

        return org.springframework.security.core.userdetails.User.builder()
            .username(user.getUsername())   // salutiamo col nickname
            .password(pwd)                  // bcrypt per locali, {noop} per OAuth
            .authorities(List.of(authority))
            .build();
    }
}
