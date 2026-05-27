package com.car.util;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
public class JWTutil {
	
	private final String jwtSignature;
	
	private final SecretKey secretKey;

	public JWTutil(@Value("${jwt.signature}") String jwtSignature) {
		this.jwtSignature = jwtSignature;
		secretKey=Keys.hmacShaKeyFor(jwtSignature.getBytes(StandardCharsets.UTF_8)); 
		//this.secretKey = secretKey;
	}
	
//	public void assignKey() {
//		secretKey=Keys.hmacShaKeyFor(jwtSignature.getBytes(StandardCharsets.UTF_8)); 
//	}
	
	public String createJwtToken(String username, List<String> role) {
		String token=Jwts.builder()
				.subject(username)
				.claim("role", role)
				.issuedAt(new Date())
				.expiration(new Date(System.currentTimeMillis()+3600*1000))
				.signWith(secretKey)
				.compact();
		return token;
	}
	public String extractUsername(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }
	public List<String> extractRoles(String token) {
           Object roles = Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .get("role");
        return (List<String>) roles;
	}
	public boolean isTokenExpired(String token) {
		Date expire=Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().getExpiration();
		return expire.before(new Date());
	}
}
