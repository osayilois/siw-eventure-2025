package com.osayi.eventure.security;

import java.util.List;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {
  @Override
  public OAuth2User loadUser(OAuth2UserRequest req) throws OAuth2AuthenticationException {
    var delegate = new DefaultOAuth2UserService();
    OAuth2User oauthUser = delegate.loadUser(req);

    Map<String, Object> attrs = oauthUser.getAttributes();
    String provider = req.getClientRegistration().getRegistrationId(); // "google" - "facebook"

    String email = str(attrs.get("email"));
    String name  = str(attrs.get("name"));
    String id    = str(attrs.get("sub")); if (id == null) id = str(attrs.get("id"));

    String principalKey = email != null ? "email" : (name != null ? "name" : (id != null ? ("google".equals(provider) ? "sub" : "id") : "name"));

    @SuppressWarnings("unchecked")
    List<GrantedAuthority> authorities = (List<GrantedAuthority>) oauthUser.getAuthorities();

    return new DefaultOAuth2User(authorities, attrs, principalKey);
  }
  private static String str(Object o){ return o==null? null : String.valueOf(o); }
}
