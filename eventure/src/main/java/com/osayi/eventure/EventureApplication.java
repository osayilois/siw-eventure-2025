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
        String adminEmail = "admin@eventure.com";
        if (userRepo.findByEmail(adminEmail).isEmpty()) {
            User admin = new User();
            admin.setEmail(adminEmail);
            admin.setUsername("admin");
            admin.setPassword(encoder.encode("Admin123!")); // cambiala subito
            admin.setRuolo(User.Role.ADMIN);
            userRepo.save(admin);
            System.out.println("ADMIN creato: " + adminEmail + " / Admin123!");
        }
    };
}

}


