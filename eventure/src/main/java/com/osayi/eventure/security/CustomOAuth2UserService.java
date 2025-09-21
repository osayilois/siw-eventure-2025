package com.osayi.eventure.security;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.osayi.eventure.model.User;
import com.osayi.eventure.model.User.AuthProvider;
import com.osayi.eventure.repository.UserRepository;

import java.util.List;

@Service
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

  private final UserRepository userRepo;

  public CustomOAuth2UserService(UserRepository userRepo) {
    this.userRepo = userRepo;
  }

  @Override
  @Transactional
  public OAuth2User loadUser(OAuth2UserRequest req) throws OAuth2AuthenticationException {
    var delegate = new DefaultOAuth2UserService();
    OAuth2User oauthUser = delegate.loadUser(req);

    Map<String, Object> attrs = oauthUser.getAttributes();
    String regId = req.getClientRegistration().getRegistrationId(); // "google" | "facebook"

    // --- mappa attributi rilevanti per i due provider ---
    AuthProvider provider;
    String email;
    String name;
    String providerId;
    String avatarUrl = null;

    if ("google".equalsIgnoreCase(regId)) {
      provider = AuthProvider.GOOGLE;
      email = str(attrs.get("email"));
      name  = str(attrs.get("name"));
      providerId = str(attrs.get("sub"));
      avatarUrl = str(attrs.get("picture")); // foto
    } else if ("facebook".equalsIgnoreCase(regId)) {
      provider = AuthProvider.FACEBOOK;
      email = str(attrs.get("email")); // può essere null
      name  = str(attrs.get("name"));
      providerId = str(attrs.get("id"));
      // URL pubblico dell’immagine profilo (redirect 302 gestito dal browser)
      if (providerId != null) {
        avatarUrl = "https://graph.facebook.com/v19.0/" + providerId + "/picture?type=large";
      }
    } else {
      throw new OAuth2AuthenticationException("Unsupported provider: " + regId);
    }

    // --- provisioning / merge ---
    User user = null;

    if (email != null) {
      user = userRepo.findByEmailIgnoreCase(email).orElse(null);
    }
    if (user == null && providerId != null) {
      user = userRepo.findByProviderAndProviderId(provider, providerId).orElse(null);
    }

    if (user == null) {
      // crea un nuovo utente
      String baseForUsername =
          (email != null && email.contains("@")) ? email.substring(0, email.indexOf('@')) :
          (name != null ? name : (regId + "_" + providerId));
      String uniqueUsername = nextUniqueUsername(slug(baseForUsername));

      user = new User();
      user.setUsername(uniqueUsername);
      // Facebook può non dare email: crea un placeholder tecnico
      user.setEmail(email != null ? email : (regId + "_" + providerId + "@oauth.local"));
      user.setPassword(null); // nessuna password per utenti social
      user.setRuolo(User.Role.USER);
      user.setProvider(provider);
      user.setProviderId(providerId);
      user.setAvatarUrl(avatarUrl);
      user = userRepo.save(user);
    } else {
      // aggiorna dati provider e avatar se mancanti
      user.setProvider(provider);
      user.setProviderId(providerId);
      if (user.getAvatarUrl() == null && avatarUrl != null) {
        user.setAvatarUrl(avatarUrl);
      }
      // opzionale: se l’utente non aveva email “vera” e ora Facebook la fornisce, aggiorna
      if (email != null && (user.getEmail() == null || user.getEmail().endsWith("@oauth.local"))) {
        // attenzione ai vincoli di unicità sull’email:
        if (!userRepo.existsByEmailIgnoreCaseAndIdNot(email, user.getId())) {
          user.setEmail(email);
        }
      }
      user = userRepo.save(user);
    }

    // --- costruisci principal "pulito": auth.getName() = username ---
    Map<String, Object> principalAttrs = new HashMap<>();
    principalAttrs.put("username", user.getUsername());
    principalAttrs.put("email", user.getEmail());
    principalAttrs.put("id", user.getId());

    // un solo ruolo base
    List<SimpleGrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_USER"));

    // IMPORTANTISSIMO: nameAttributeKey = "username"
    return new DefaultOAuth2User(authorities, principalAttrs, "username");
  }

  // Helpers
  private static String str(Object o) { return (o == null) ? null : String.valueOf(o); }

  private String slug(String s) {
    if (s == null) return "user";
    String t = s.toLowerCase(Locale.ITALIAN).replaceAll("[^a-z0-9]+", "");
    return t.isBlank() ? "user" : t;
  }

  private String nextUniqueUsername(String base) {
    String cand = base;
    int i = 1;
    while (userRepo.existsByUsernameIgnoreCase(cand)) {
      cand = base + i++;
    }
    return cand;
  }
}
