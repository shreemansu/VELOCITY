package com.car.serviceimpl;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.car.dto.LoginResponseDto;
import com.car.entity.User;
import com.car.repository.UserRepository;
import com.car.service.AuthService;
import com.car.util.JWTutil;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
	
	private final UserRepository userRepo;
	
	private final JWTutil jwTutil;
	
	private final AuthenticationManager authManager;
	
	@Qualifier("invalidjwt")
	private final Set<String> blockedJwt;
	
	
	@Override
	public LoginResponseDto loginWithCredentialsService(String email, String password) {
		UsernamePasswordAuthenticationToken authToken=new UsernamePasswordAuthenticationToken(email, password);
		Authentication authentication=authManager.authenticate(authToken);
		if(authentication.isAuthenticated()) {
			List<String> role=authentication.getAuthorities()
					.stream()
					.map(authority->authority.getAuthority()).toList();
			String jwt=jwTutil.createJwtToken(email, role);
			User user=userRepo.findByEmail(email).orElseThrow(()-> new RuntimeException("user Not Found"));
			return new LoginResponseDto(jwt, user.getName(), user.getId());
		}
		throw new RuntimeException("Invalid password");
	}

	@Override
	public String logoutService(HttpServletRequest request) {
		String authHeader=request.getHeader(HttpHeaders.AUTHORIZATION);
		String jwt=null;
		if(authHeader!=null && authHeader.startsWith("Bearer ")) jwt=authHeader.substring(7);
		if(blockedJwt.contains(jwt)) return "Already Logged Out!";
		blockedJwt.add(jwt);
		return "You have been logged out successfully!";
	}
	
}
