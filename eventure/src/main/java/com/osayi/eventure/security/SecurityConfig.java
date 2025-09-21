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
                .requestMatchers(
                        "/", "/home", "/login", "/register", "/error", "/favicon.ico",
                        "/forgot-password", "/reset-password/**" 
                ).permitAll()

                // Eventi pubblici (lista + dettagli)
                .requestMatchers(HttpMethod.GET, "/eventi", "/eventi/**").permitAll()

                // Statici (css/js/img + upload avatar locali)
                .requestMatchers("/css/**", "/js/**", "/images/**", "/img/**", "/webjars/**", "/uploads/**").permitAll()

                // --- ADMIN ---
                .requestMatchers("/eventi/nuovo", "/eventi/modifica/**", "/eventi/elimina/**", "/admin/**")
                    .hasRole("ADMIN")

                // --- USER ---
                .requestMatchers("/user/**").hasRole("USER")

                // --- caricamento avatar in locale ---
                .requestMatchers("/uploads/**").permitAll()

                // --- ACCOUNT (nuove rotte profilo + API) ---
                .requestMatchers("/account","/account/**", "/api/account/**").hasRole("USER")

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
                .userInfoEndpoint(u -> u.userService(customOAuth2UserService))
                // Se vuoi il redirect fisso su /account, spostalo nel tuo AuthSuccessHandler
                .defaultSuccessUrl("/", false)
            )

            // Logout
            .logout(logout -> logout
                .logoutSuccessUrl("/home")
                .permitAll()
            )

            // CSRF: escludi le API REST (inclusi upload avatar)
            .csrf(csrf -> csrf.ignoringRequestMatchers("/h2-console/**", "/api/**"));

        // H2 console in dev
        http.headers(headers -> headers.frameOptions(frame -> frame.sameOrigin()));

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
