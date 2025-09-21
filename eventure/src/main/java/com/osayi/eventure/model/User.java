package com.osayi.eventure.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "users")
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // username e email univoci (puoi rimuovere unique=true se hai esigenze diverse)
    @Column(unique = true, nullable = false, length = 64)
    private String username;

    // Attenzione: con Facebook l'email può mancare -> lascio nullable=true
    @Column(unique = true, nullable = true, length = 255)
    private String email;

    // Per utenti OAuth la password può essere nulla (gestita dal provider)
    @Column(nullable = true, length = 255)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 16)
    private Role ruolo = Role.USER;

    // ==== OAuth / Social ====

    // Provider di autenticazione: LOCAL, GOOGLE, FACEBOOK
    @Enumerated(EnumType.STRING)
    @Column(nullable = true, length = 16)
    private AuthProvider provider; // null o LOCAL/GOOGLE/FACEBOOK

    // "sub" (Google) o "id" (Facebook). Utile per fare merge account.
    @Column(nullable = true, length = 128)
    private String providerId;

    // Può essere un URL esterno (Google/Facebook) o un path locale
    // (/uploads/avatars/uuid.jpg)
    @Column(nullable = true, length = 512)
    private String avatarUrl;

    public enum Role {
        USER,
        ADMIN
    }

    public enum AuthProvider {
        LOCAL,
        GOOGLE,
        FACEBOOK
    }

    // --- Helper opzionali (comodi in controller/service) ---
    public boolean isLocal() {
        return provider == null || provider == AuthProvider.LOCAL;
    }

    public boolean isSocial() {
        return provider == AuthProvider.GOOGLE || provider == AuthProvider.FACEBOOK;
    }
}
