/* /* package com.osayi.eventure.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    // usiamo il CustomUserDetailsService che è annotato con @Service
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/eventi/nuovo", "/eventi/modifica/**", "/eventi/elimina/**").hasRole("ADMIN")
                .requestMatchers("/", "/eventi", "/register", "/login", "/css/**", "/js/**").permitAll()
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/login")
                .usernameParameter("username")   // l'input name che il form invia (user email)
                .passwordParameter("password")
                .defaultSuccessUrl("/eventi", true)
                .permitAll()
            )
            .logout(logout -> logout
                .logoutSuccessUrl("/login?logout")
                .permitAll()
            )
            .csrf(csrf -> csrf.ignoringRequestMatchers("/h2-console/**")); // opzionale per dev

        // se usi H2 console in dev, puoi abilitare frame options:
        http.headers(headers -> headers.frameOptions(frame -> frame.sameOrigin()));

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
 */


/* package com.osayi.eventure.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private AuthSuccessHandler authSuccessHandler; // redirect dopo login

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                // ADMIN
                .requestMatchers("/eventi/nuovo", "/eventi/modifica/**", "/eventi/elimina/**", "/admin/**")
                    .hasRole("ADMIN")
                // USER
                .requestMatchers("/user/**").hasRole("USER")
                // Pagine pubbliche
                .requestMatchers("/", "/home", "/eventi", "/register", "/login", "/css/**", "/js/**", "/images/**")
                    .permitAll()
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/login")
                .usernameParameter("username")   // nome input form
                .passwordParameter("password")
                .successHandler(authSuccessHandler) // <-- redirect personalizzato
                .permitAll()
            )
            .logout(logout -> logout
                .logoutSuccessUrl("/login?logout")
                .permitAll()
            )
            .csrf(csrf -> csrf.ignoringRequestMatchers("/h2-console/**")); // opzionale per dev

        // Se usi H2 console
        http.headers(headers -> headers.frameOptions(frame -> frame.sameOrigin()));

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
 */

// Configurazione della sicurezza: regole di accesso, login/logout e gestione password

package com.osayi.eventure.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private AuthSuccessHandler authSuccessHandler; // Gestore redirect dopo login

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                // Pagine pubbliche accessibili a tutti
                .requestMatchers("/", "/home", "/eventi", "/register", "/login",
                                 "/css/**", "/js/**", "/images/**").permitAll()

                // Pagine solo per ADMIN
                .requestMatchers("/eventi/nuovo", "/eventi/modifica/**", "/eventi/elimina/**", "/admin/**")
                    .hasRole("ADMIN")

                // Pagine solo per USER
                .requestMatchers("/user/**").hasRole("USER")

                // Qualsiasi altra richiesta richiede login
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/login")
                .usernameParameter("username") // Nome input nel form di login
                .passwordParameter("password")
                .successHandler(authSuccessHandler) // Redirect personalizzato dopo login
                .permitAll()
            )
            .logout(logout -> logout
                .logoutSuccessUrl("/home") // torna alla home pubblica dopo logout
                .permitAll()
            )
            // Disattiva CSRF per H2 console (solo sviluppo)
            .csrf(csrf -> csrf.ignoringRequestMatchers("/h2-console/**"));

        // Necessario per visualizzare H2 console
        http.headers(headers -> headers.frameOptions(frame -> frame.sameOrigin()));

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
