/* // Configurazione della sicurezza: regole di accesso, login/logout e gestione password

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
 */


 // Configurazione della sicurezza: regole di accesso, login/logout e gestione password
package com.osayi.eventure.security;

import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private AuthSuccessHandler authSuccessHandler; // Gestore redirect dopo login

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                // --- AREE PUBBLICHE ---
                .requestMatchers("/", "/home", "/login", "/register").permitAll()
                // pagine pubbliche degli eventi (lista + dettagli)
                .requestMatchers(HttpMethod.GET, "/eventi", "/eventi/**").permitAll()
                // risorse statiche
                .requestMatchers("/css/**", "/js/**", "/images/**", "/img/**", "/webjars/**").permitAll()

                // --- AREE ADMIN (tutte le HTTP method) ---
                .requestMatchers("/eventi/nuovo", "/eventi/modifica/**", "/eventi/elimina/**", "/admin/**")
                    .hasRole("ADMIN")

                // --- AREE USER ---
                .requestMatchers("/user/**").hasRole("USER")

                // qualsiasi altra rotta richiede login
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/login")                 // tua pagina login
                .usernameParameter("username")
                .passwordParameter("password")
                .successHandler(authSuccessHandler)   // redirect personalizzato
                .permitAll()
            )
            .logout(logout -> logout
                // /logout (POST) è quello di default
                .logoutSuccessUrl("/home")            // dopo logout → home pubblica
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
