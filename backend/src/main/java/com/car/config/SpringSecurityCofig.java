package com.car.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.car.filters.JWTfilter;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class SpringSecurityCofig {
	
	private final JWTfilter jwTfilter;
	
	@Bean
	public PasswordEncoder createPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}
	@Bean
	public SecurityFilterChain configureFilterChain(HttpSecurity httpSecurity) throws Exception {
		httpSecurity.cors(cors->cors.configurationSource(corsConfigurationSource()))
		.csrf(csrf->csrf.disable())
		.authorizeHttpRequests(//auth->auth.anyRequest().permitAll()
				auth->auth.requestMatchers("/api/v2/car/user/registration",
						"/api/v2/car/user/verification",
						"/api/v3/car/auth/login",
						"/swagger-ui/**",
						"/swagger-ui.html",
						"/v3/api-docs/**",
						"/webjars/**").permitAll()
				.requestMatchers(HttpMethod.GET,"/api/v1/car/**").hasAnyRole("USER","ADMIN")
				.requestMatchers(HttpMethod.POST,"/api/v1/car/**").hasRole("ADMIN")
				.requestMatchers(HttpMethod.DELETE,"/api/v1/car/**").hasRole("ADMIN")
				.anyRequest().authenticated()
				)
		.formLogin(form->form.disable())
		.httpBasic(basic->basic.disable())
		.sessionManagement(session->session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
		.addFilterBefore(jwTfilter, UsernamePasswordAuthenticationFilter.class);
		return httpSecurity.build();
	}
	@Bean
	public AuthenticationManager createAuthManager(AuthenticationConfiguration authConfig) throws Exception {
		return authConfig.getAuthenticationManager();
	}
	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		
		CorsConfiguration corsConfig=new CorsConfiguration();
		corsConfig.setAllowedOrigins(List.of("http://localhost:5500","http://127.0.0.1:5500"));
		corsConfig.setAllowedMethods(List.of("GET","POST","PUT","DELETE","PATCH"));
		corsConfig.setAllowedHeaders(List.of("*"));
		corsConfig.setExposedHeaders(List.of("Authorization"));
		
		UrlBasedCorsConfigurationSource source=new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", corsConfig);
		return source;
	}
}
