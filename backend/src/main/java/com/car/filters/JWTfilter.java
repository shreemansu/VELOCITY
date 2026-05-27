package com.car.filters;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.car.util.JWTutil;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JWTfilter extends OncePerRequestFilter{
	
	private final JWTutil jwtUtil;
	
	@Qualifier("invalidjwt")
	private final Set<String> blockedJwt;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
		String authHeader=request.getHeader(HttpHeaders.AUTHORIZATION);
		String jwt=null;
		
		if(authHeader!=null && authHeader.startsWith("Bearer ")) jwt=authHeader.substring(7);
		
		if(jwt==null) {
			filterChain.doFilter(request, response);
			return;
		}
		
		try {
//			if(jwtUtil.isTokenExpired(jwt)||blockedJwt.contains(jwt)) {
//				response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//				response.setContentType("application/json");
//				response.getWriter().write("{\"message\":\"JWT expired\"}");
//			}
			String username=jwtUtil.extractUsername(jwt);
			if(username!=null && !jwtUtil.isTokenExpired(jwt) && SecurityContextHolder.getContext().getAuthentication()==null) {
				List<String> roles=jwtUtil.extractRoles(jwt);
				List<SimpleGrantedAuthority> authorities=roles.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
				UsernamePasswordAuthenticationToken authToken=new UsernamePasswordAuthenticationToken(username, null, authorities);
				authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				SecurityContextHolder.getContext().setAuthentication(authToken);
				
				System.out.println("Token: " + jwt);
				System.out.println("Username: " + username);
				System.out.println("Roles: " + roles);
			}
		} catch (Exception e) {
//			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//			response.setContentType("application/json");
//			response.getWriter().write("{\"message\":\"JWT invalid\"}");
//			return;
			System.out.println("JWT Error: "+e.getMessage());
		}
		
		filterChain.doFilter(request, response);
	}
	@Override
	protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
		String path=request.getRequestURI();
		return path.startsWith("/api/v3/car/auth/login");
	}

}
