package com.glsid.digital_banking.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Service
public class OAuth2JwtService {
    
    private final JwtEncoder jwtEncoder;
    
    @Value("${application.security.jwt.expiration}")
    private long jwtExpiration;
    
    @Value("${application.security.jwt.refresh-token.expiration}")
    private long refreshExpiration;
    
    @Value("${application.security.jwt.issuer}")
    private String issuer;
    
    @Value("${application.security.jwt.audience}")
    private String audience;
    
    public OAuth2JwtService(JwtEncoder jwtEncoder) {
        this.jwtEncoder = jwtEncoder;
    }
    
    public String generateToken(UserDetails userDetails) {
        return generateToken(userDetails, jwtExpiration);
    }
    
    public String generateRefreshToken(UserDetails userDetails) {
        return generateToken(userDetails, refreshExpiration);
    }
    
    private String generateToken(UserDetails userDetails, long expiration) {
        Instant now = Instant.now();
        Instant expiresAt = now.plus(expiration, ChronoUnit.MILLIS);
        
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer(issuer)
                .issuedAt(now)
                .expiresAt(expiresAt)
                .subject(userDetails.getUsername())
                .audience(java.util.List.of(audience))
                .claim("scope", userDetails.getAuthorities().stream()
                        .map(authority -> authority.getAuthority())
                        .toList())
                .build();
        
        return jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }
    
    public long getJwtExpiration() {
        return jwtExpiration;
    }
}
