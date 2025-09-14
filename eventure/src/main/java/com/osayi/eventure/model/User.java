/* package com.osayi.eventure.model;

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

    private String username;
    private String email;
    private String password;

    @Enumerated(EnumType.STRING)
    private Role ruolo;

    public enum Role {
        USER,
        ADMIN
    }
}
 */

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

    private String username;
    private String email;

    // Per utenti OAuth la password può essere nulla
    @Column(nullable = true)
    private String password;

    @Enumerated(EnumType.STRING)
    private Role ruolo;

    // ==== NEW: info provenienza OAuth ====
    // "google" | "facebook" | null (LOCAL)
    private String provider;

    // "sub" Google o "id" Facebook
    private String providerId;

    public enum Role {
        USER,
        ADMIN
    }
}
