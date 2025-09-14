package com.osayi.eventure.security;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Locale;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.osayi.eventure.model.User;
import com.osayi.eventure.repository.UserRepository;

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
    final String provider = req.getClientRegistration().getRegistrationId(); // "google" | "facebook"

    final String email = str(attrs.get("email"));
    final String name  = str(attrs.get("name"));
    // calcola una VARIABILE FINALE per l'id del provider
    final String providerId = firstNonNull(str(attrs.get("sub")), str(attrs.get("id"))); // Google usa "sub", Facebook "id"

    // === PROVISIONING JIT ===
    Optional<User> existing =
        (email != null) ? userRepo.findByEmailIgnoreCase(email) : Optional.empty();

    if (existing.isEmpty() && providerId != null) {
      existing = userRepo.findByProviderAndProviderId(provider, providerId);
    }

    User user = existing.orElseGet(() -> {
      String effectiveEmail = (email != null) ? email : (provider + "_" + providerId + "@oauth.local");

      String base = (email != null && email.contains("@"))
          ? email.substring(0, email.indexOf('@'))
          : (name != null ? name : provider + "_" + providerId);

      String uniqueUsername = uniqueUsername(slug(base));

      User u = new User();
      u.setEmail(effectiveEmail);
      u.setUsername(uniqueUsername);
      u.setPassword(null); // nessuna password per OAuth
      u.setRuolo(User.Role.USER);
      u.setProvider(provider);
      u.setProviderId(providerId);
      return userRepo.save(u);
    });

    // quale chiave usare come "name" per Principal
    String principalKey = (email != null) ? "email"
        : (name != null) ? "name"
        : (providerId != null ? ("google".equals(provider) ? "sub" : "id") : "name");

    @SuppressWarnings("unchecked")
    List<GrantedAuthority> authorities = (List<GrantedAuthority>) oauthUser.getAuthorities();

    return new DefaultOAuth2User(authorities, attrs, principalKey);
  }

  private static String str(Object o){ return (o == null) ? null : String.valueOf(o); }
  private static String firstNonNull(String a, String b){ return (a != null) ? a : b; }

  // ---- Helpers per username univoco ----
  private String slug(String s){
    if (s == null) return "user";
    String t = s.toLowerCase(Locale.ITALIAN).replaceAll("[^a-z0-9]+", "");
    return t.isBlank() ? "user" : t;
  }

  private String uniqueUsername(String base){
    String cand = base;
    int i = 1;
    while (userRepo.existsByUsernameIgnoreCase(cand)) {
      cand = base + i++;
    }
    return cand;
  }
}
