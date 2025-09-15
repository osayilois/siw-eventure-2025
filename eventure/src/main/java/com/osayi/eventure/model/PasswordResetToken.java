package com.osayi.eventure.model;

import jakarta.persistence.*;
import java.time.Instant;
import lombok.Data;

@Entity
@Data
@Table(name = "password_reset_tokens", indexes = {
  @Index(name="idx_prt_token", columnList="token", unique=true)
})
public class PasswordResetToken {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable=false, unique=true, length=120)
  private String token;

  @ManyToOne(optional=false, fetch = FetchType.LAZY)
  private User user;

  @Column(nullable=false)
  private Instant expiresAt;

  @Column(nullable=false)
  private boolean used = false;
}
