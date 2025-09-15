package com.osayi.eventure.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final AuthSuccessHandler authSuccessHandler;
    private final CustomOAuth2UserService customOAuth2UserService;

    // 👉 Constructor injection: Spring passa i bean qui
    public SecurityConfig(AuthSuccessHandler authSuccessHandler,
                          CustomOAuth2UserService customOAuth2UserService) {
        this.authSuccessHandler = authSuccessHandler;
        this.customOAuth2UserService = customOAuth2UserService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                // --- AREE PUBBLICHE ---
                .requestMatchers("/", "/home", "/login", "/register", "/error", "/favicon.ico", "/forgot-password", "reset-password/**").permitAll()
                // Eventi pubblici (lista + dettagli)
                .requestMatchers(HttpMethod.GET, "/eventi", "/eventi/**").permitAll()
                // Statici
                .requestMatchers("/css/**", "/js/**", "/images/**", "/img/**", "/webjars/**").permitAll()

                // --- ADMIN ---
                .requestMatchers("/eventi/nuovo", "/eventi/modifica/**", "/eventi/elimina/**", "/admin/**")
                    .hasRole("ADMIN")

                // --- USER ---
                .requestMatchers("/user/**").hasRole("USER")

                // resto protetto
                .anyRequest().authenticated()
            )

            // Form login classico
            .formLogin(form -> form
                .loginPage("/login")
                .usernameParameter("username")
                .passwordParameter("password")
                .successHandler(authSuccessHandler)
                .permitAll()
            )

            // OAuth2 (Google + Facebook)
            .oauth2Login(oauth -> oauth
                .loginPage("/login")
                .userInfoEndpoint(u -> u.userService(customOAuth2UserService)) // <-- qui il service
                .defaultSuccessUrl("/", false)
            )

            // Logout
            .logout(logout -> logout
                .logoutSuccessUrl("/home")
                .permitAll()
            )

            // H2 console in dev
            .csrf(csrf -> csrf.ignoringRequestMatchers("/h2-console/**"));

        http.headers(headers -> headers.frameOptions(frame -> frame.sameOrigin()));

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
