// questa è un’interfaccia che permette di personalizzare cosa succede subito dopo un login riuscito

package com.osayi.eventure.security;

import java.io.IOException;
import java.util.Collection;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class AuthSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException {

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        String role = authorities.iterator().next().getAuthority();

        switch (role) {
            case "ROLE_ADMIN" -> response.sendRedirect("/admin/home");
            case "ROLE_USER" -> response.sendRedirect("/user/home");
            default -> response.sendRedirect("/home");
        }
    }
}
