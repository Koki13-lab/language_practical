package com.forgeon.membermanagement.security;

import java.io.IOException;
import java.util.Collection;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class LoginHandler implements AuthenticationSuccessHandler {

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException {

		Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

		String redirectUrl = "/";

		if (authorities.stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
			redirectUrl = "/admin/home";
		} else if (authorities.stream().anyMatch(a -> a.getAuthority().equals("ROLE_USER"))) {
			redirectUrl = "/user/home";
		}

		response.sendRedirect(redirectUrl);

	}

}
