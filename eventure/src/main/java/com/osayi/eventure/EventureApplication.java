package com.osayi.eventure;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.osayi.eventure.model.User;
import com.osayi.eventure.repository.UserRepository;



@SpringBootApplication
public class EventureApplication {

	public static void main(String[] args) {
		SpringApplication.run(EventureApplication.class, args);
	}

@Bean
public CommandLineRunner seedAdmin(UserRepository userRepo, PasswordEncoder encoder) {
    return args -> {
        final String adminEmail = "admin@eventure.com".trim().toLowerCase();
        final String adminUsername = "admin";

        // Crea l'admin solo se NON esiste né per email né per username
        boolean emailEsiste = userRepo.existsByEmailIgnoreCase(adminEmail);
        boolean userEsiste  = userRepo.existsByUsernameIgnoreCase(adminUsername);

        if (!emailEsiste && !userEsiste) {
            User admin = new User();
            admin.setEmail(adminEmail);
            admin.setUsername(adminUsername);
            admin.setPassword(encoder.encode("Admin123!")); // BCrypt
            admin.setRuolo(User.Role.ADMIN);
            userRepo.save(admin);
            System.out.println("ADMIN creato: " + adminEmail + " / Admin123!");
        } else {
            // Se esiste già per email, assicura il ruolo ADMIN (utile in seed ripetuti)
            userRepo.findByEmailIgnoreCase(adminEmail).ifPresent(u -> {
                if (u.getRuolo() != User.Role.ADMIN) {
                    u.setRuolo(User.Role.ADMIN);
                    userRepo.save(u);
                    System.out.println("ADMIN aggiornato (ruolo -> ADMIN).");
                }
            });
        }
    };
}

}


