package com.forgeon.membermanagement.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

	@Autowired
	private LoginHandler loginHandler;

	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http.authorizeHttpRequests(auth -> auth.requestMatchers("/", "/top", "/login**").permitAll()
				.requestMatchers("/css/**").permitAll().requestMatchers("/admin/**").hasRole("ADMIN")
				.requestMatchers("/user/**").hasRole("USER").anyRequest().authenticated())
				.formLogin(form -> form.loginPage("/login").permitAll().successHandler(loginHandler))
				.logout(logout -> logout.logoutUrl("/logout").logoutSuccessUrl("/top").invalidateHttpSession(true)
						.deleteCookies("JSESSIONID"))
				.exceptionHandling(ex -> ex.accessDeniedHandler((request, response, exception) -> {

					Authentication auth = SecurityContextHolder.getContext().getAuthentication();

					boolean isAdmin = auth.getAuthorities().stream()
							.anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

					if (isAdmin) {
						response.sendRedirect("/admin/home");
					} else {
						response.sendRedirect("/user/home");
					}
				}));
		return http.build();
	}
}